package turing.enchantmentlib.mixin;

import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.entity.projectile.EntityArrow;
import net.minecraft.core.entity.projectile.EntityArrowGolden;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemBow;
import net.minecraft.core.item.ItemQuiver;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import turing.enchantmentlib.EnchantmentLib;
import turing.enchantmentlib.api.EnchantmentData;

@Mixin(value = ItemBow.class, remap = false)
public class ItemBowMixin extends Item {
	public ItemBowMixin(int id) {
		super(id);
	}

	@Inject(method = "onItemRightClick", at = @At("HEAD"), cancellable = true)
	public void onBowUse(ItemStack stack, World world, EntityPlayer player, CallbackInfoReturnable<ItemStack> cir) {
		if (EnchantmentLib.isItemEnchanted(stack)) {
			EnchantmentData[] enchantments = EnchantmentLib.getEnchantsForItem(stack);
			boolean hasAmmo = false;
			boolean gold = false;
			boolean shouldCollect = true;
			for (EnchantmentData enchantment : enchantments) {
				if (enchantment.getEnchantment().doesHaveAmmo(stack, player, world)) {
					hasAmmo = true;
				}
				if (enchantment.getEnchantment().shouldArrowBeGolden(stack, player, world)) {
					gold = true;
				}
				if (!enchantment.getEnchantment().shouldCollectArrows(stack, player, world)) {
					shouldCollect = false;
				}
			}

			if (!hasAmmo) {
				ItemStack quiver = player.inventory.armorItemInSlot(2);
				if (quiver != null && quiver.getItem() instanceof ItemQuiver && quiver.getMetadata() < quiver.getMaxDamage())
					hasAmmo = true;
				else if (player.inventory.consumeInventoryItem(Item.ammoArrowGold.id)) {
					hasAmmo = true;
					gold = true;
				} else if (player.inventory.consumeInventoryItem(Item.ammoArrow.id))
					hasAmmo = true;
				if (hasAmmo) shouldCollect = true;
			}

			if (hasAmmo) {
				stack.damageItem(1, player);
				world.playSoundAtEntity(player, player, "random.bow", 0.3F, 1.0F / (itemRand.nextFloat() * 0.4F + 0.8F));
				if (!world.isClientSide) {
					EntityArrow arrow = gold ? new EntityArrowGolden(world, player, shouldCollect) : new EntityArrow(world, player, shouldCollect, 0);
					world.entityJoinedWorld(arrow);
					for (EnchantmentData enchantment : enchantments) {
						enchantment.getEnchantment().onArrowFired(stack, player, world, arrow);
					}
				}
			}

			cir.setReturnValue(stack);
		}
	}
}
