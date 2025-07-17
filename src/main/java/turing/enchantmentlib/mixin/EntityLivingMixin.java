package turing.enchantmentlib.mixin;

import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.EntityItem;
import net.minecraft.core.entity.Mob;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import turing.enchantmentlib.EnchantmentLib;
import turing.enchantmentlib.api.EnchantmentData;

import java.util.ArrayList;
import java.util.List;

@Mixin(value = Mob.class, remap = false)
public class EntityLivingMixin extends EntityMixin {
	@Unique
	public ThreadLocal<ItemStack> hitWith = new ThreadLocal<>();

	@Inject(method = "onDeath", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/entity/Mob;dropDeathItems()V", shift = At.Shift.BEFORE))
	public void beforeDrops(Entity entity, CallbackInfo ci) {
		if (entity instanceof Player && !world.isClientSide) {
			Player player = (Player) entity;
			hitWith.set(player.getHeldItem());
			dropList.set(new ArrayList<>());
		}
	}

	@Inject(method = "onDeath", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/entity/Mob;dropDeathItems()V", shift = At.Shift.AFTER))
	public void afterDrops(Entity entity, CallbackInfo ci) {
		if (entity instanceof Player && !world.isClientSide) {
			ItemStack stack = hitWith.get();
			List<ItemStack> base = dropList.get();
			if (EnchantmentLib.isItemEnchanted(stack)) {
				List<ItemStack> extra = new ArrayList<>();
				for (EnchantmentData enchantment : EnchantmentLib.getEnchantsForItem(stack)) {
					enchantment.getEnchantment().getExtraEntityDrops(stack, (Mob)(Object)this, base, extra);
				}
				extra.forEach(this::spawnItem);
			}
			dropList.remove();
			hitWith.remove();
		}
	}

	@Unique
	public void spawnItem(ItemStack stack) {
		EntityItem entityItem = new EntityItem(world, x, y, z, stack);
		entityItem.pickupDelay = 10;
		world.entityJoinedWorld(entityItem);
	}
}
