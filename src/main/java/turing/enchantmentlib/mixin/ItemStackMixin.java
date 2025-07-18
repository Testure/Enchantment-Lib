package turing.enchantmentlib.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.achievement.stat.StatList;
import net.minecraft.core.block.Block;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.Mob;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import turing.enchantmentlib.EnchantmentLib;
import turing.enchantmentlib.api.EnchantmentData;

@Mixin(value = ItemStack.class, remap = false)
public abstract class ItemStackMixin {
	@Shadow
	private int metadata;

	@Shadow
	public abstract boolean isItemStackDamageable();

	@Unique
	public final ItemStack thisAs = (ItemStack)(Object)this;

	@Inject(method = "damageItem", at = @At("HEAD"), cancellable = true)
	public void onItemDamage(int damageToTake, Entity entity, CallbackInfo ci) {
		if (isItemStackDamageable() && (!(entity instanceof Player) || ((Player) entity).gamemode.toolDurability())) {
			if (EnchantmentLib.isItemEnchanted(thisAs)) {
				int newDamageToTake = damageToTake;
				for (EnchantmentData enchantment : EnchantmentLib.getEnchantsForItem(thisAs)) {
					int i = enchantment.getEnchantment().onItemDamage(thisAs, newDamageToTake);
					if (i != newDamageToTake) newDamageToTake = i;
				}
				if (newDamageToTake != damageToTake) {
					metadata += Math.max(newDamageToTake, 0);
					ci.cancel();
				}
			}
		}
	}

	@Inject(method = "repairItem", at = @At("HEAD"), cancellable = true)
	public void onRepairItem(int damageToRepair, CallbackInfo ci) {
		if (isItemStackDamageable()) {
			if (EnchantmentLib.isItemEnchanted(thisAs)) {
				int finalRepair = damageToRepair;
				for (EnchantmentData enchantment : EnchantmentLib.getEnchantsForItem(thisAs)) {
					int i = enchantment.getEnchantment().onRepairItem(thisAs, finalRepair);
					if (i != finalRepair) finalRepair = i;
				}
				if (finalRepair != damageToRepair) {
					metadata = Math.max(metadata - finalRepair, 0);
					ci.cancel();
				}
			}
		}
	}

	@Inject(method = "getDamageVsEntity", at = @At("HEAD"), cancellable = true)
	public void getDamage(Entity entity, CallbackInfoReturnable<Integer> cir) {
		int og = thisAs.getItem().getDamageVsEntity(entity, thisAs);
		for (EnchantmentData enchantment : EnchantmentLib.getEnchantsForItem(thisAs)) {
			int change = enchantment.getEnchantment().getAttackDamage(thisAs, entity, og);
			if (change != og) og += change;
		}
		cir.setReturnValue(og);
	}

	@Inject(method = "canHarvestBlock", at = @At("HEAD"), cancellable = true)
	public void canHarvestBlock(Mob mob, Block<?> block, CallbackInfoReturnable<Boolean> cir) {
		boolean og = thisAs.getItem().canHarvestBlock(mob, thisAs, block);
		for (EnchantmentData enchantment : EnchantmentLib.getEnchantsForItem(thisAs)) {
			boolean change = enchantment.getEnchantment().canHarvestBlock(thisAs, block, og);
			if (change != og) og = change;
		}
		cir.setReturnValue(og);
	}

	@Inject(method = "hitEntity", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/entity/player/Player;addStat(Lnet/minecraft/core/achievement/stat/Stat;I)V", shift = At.Shift.BEFORE))
	public void hitEntity(Mob entity, Player player, CallbackInfo ci) {
		if (EnchantmentLib.isItemEnchanted(thisAs)) {
			for (EnchantmentData enchantment : EnchantmentLib.getEnchantsForItem(thisAs)) {
				enchantment.getEnchantment().onHitEntity(thisAs, player, entity, true);
			}
		}
	}

	@Inject(method = "hitEntity", at = @At("TAIL"))
	public void afterHitEntity(Mob entity, Player player, CallbackInfo ci, @Local boolean flag) {
		if (!flag) {
			if (EnchantmentLib.isItemEnchanted(thisAs)) {
				for (EnchantmentData enchantment : EnchantmentLib.getEnchantsForItem(thisAs)) {
					enchantment.getEnchantment().onHitEntity(thisAs, player, entity, false);
				}
			}
		}
	}

	@Inject(method = "useItemOnEntity", at = @At("HEAD"), cancellable = true)
	public void useOnEntity(Mob entity, Player player, CallbackInfoReturnable<Boolean> cir) {
		boolean og = thisAs.getItem().useItemOnEntity(thisAs, entity, player);
		for (EnchantmentData enchantment : EnchantmentLib.getEnchantsForItem(thisAs)) {
			boolean change = enchantment.getEnchantment().onUseOnEntity(thisAs, player, entity, og);
			if (change != og) og = change;
		}
		cir.setReturnValue(og);
	}

	@Inject(method = "beforeDestroyBlock", at = @At("HEAD"), cancellable = true)
	public void beforeDestroyBlock(World world, int id, int x, int y, int z, Side side, Player player, CallbackInfoReturnable<Boolean> cir) {
		boolean og = thisAs.getItem().beforeDestroyBlock(world, thisAs, id, x, y, z, side, player);
		for (EnchantmentData enchantment : EnchantmentLib.getEnchantsForItem(thisAs)) {
			boolean change = enchantment.getEnchantment().beforeDestroyBlock(thisAs, world, x, y, z, player, og);
			if (og != change) og = change;
		}
		cir.setReturnValue(og);
	}

	@Inject(method = "onDestroyBlock", at = @At("HEAD"), cancellable = true)
	public void onDestroyBlockDid(World world, int id, int x, int y, int z, Side side, Player player, CallbackInfo ci) {
		boolean flag = thisAs.getItem().onBlockDestroyed(world, thisAs, id, x, y, z, side, player);
		if (EnchantmentLib.isItemEnchanted(thisAs)) {
			for (EnchantmentData enchantment : EnchantmentLib.getEnchantsForItem(thisAs)) {
				boolean change = enchantment.getEnchantment().onDestroyBlock(thisAs, world, id, x, y, z, player, flag);
				if (change != flag) flag = change;
			}
		}
		if (flag) {
			player.addStat(thisAs.getItem().getStat("stat_used"), 1);
		}
		ci.cancel();
	}

	@Inject(method = "useItemRightClick", at = @At("HEAD"), cancellable = true)
	public void onRightClick(World world, Player player, CallbackInfoReturnable<ItemStack> cir) {
		ItemStack og = thisAs.getItem().onUseItem(thisAs, world, player);
		for (EnchantmentData enchantment : EnchantmentLib.getEnchantsForItem(thisAs)) {
			og = enchantment.getEnchantment().onRightClick(og, world, player);
		}
		cir.setReturnValue(og);
	}

	@Inject(method = "useItem", at = @At("HEAD"), cancellable = true)
	public void use(Player player, World world, int x, int y, int z, Side side, double xPlaced, double yPlaced, CallbackInfoReturnable<Boolean> cir) {
		boolean flag = thisAs.getItem().onUseItemOnBlock(thisAs, player, world, x, y, z, side, xPlaced, yPlaced);
		for (EnchantmentData enchantment : EnchantmentLib.getEnchantsForItem(thisAs)) {
			boolean change = enchantment.getEnchantment().onUse(thisAs, world, player, x, y, z, side, xPlaced, yPlaced, flag);
			if (change) flag = true;
		}
		if (flag) {
			player.addStat(thisAs.getItem().getStat("stat_used"), 1);
		}
		cir.setReturnValue(flag);
	}

	@Inject(method = "getStrVsBlock", at = @At("HEAD"), cancellable = true)
	public void getMiningSpeed(Block block, CallbackInfoReturnable<Float> cir) {
		float og = thisAs.getItem().getStrVsBlock(thisAs, block);
		for (EnchantmentData enchantment : EnchantmentLib.getEnchantsForItem(thisAs)) {
			float change = enchantment.getEnchantment().getMiningStrength(thisAs, block, og);
			if (change != og) og += change;
		}
		cir.setReturnValue(og);
	}
}
