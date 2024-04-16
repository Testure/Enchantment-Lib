package turing.enchantmentlib.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.entity.EntityLiving;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.InventoryPlayer;
import net.minecraft.core.util.helper.DamageType;
import net.minecraft.core.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import turing.enchantmentlib.EnchantmentLib;
import turing.enchantmentlib.api.EnchantmentData;

@Mixin(value = EntityPlayer.class, remap = false)
public class EntityPlayerMixin extends EntityLiving {
	@Shadow
	public InventoryPlayer inventory;

	public final EntityPlayer thisAs = (EntityPlayer)(Object)this;

	public EntityPlayerMixin(World world) {
		super(world);
	}

	@Inject(method = "damageEntity", at = @At(value = "RETURN", shift = At.Shift.BEFORE), cancellable = true)
	public void onDamage(int damage, DamageType damageType, CallbackInfo ci, @Local(name = "newDamage") int newDamage) {
		int finalDamage = newDamage;
		for (ItemStack stack : inventory.armorInventory) {
			if (stack != null && stack.getItem() != null) {
				for (EnchantmentData enchantment : EnchantmentLib.getEnchantsForItem(stack)) {
					int enchantDamage = enchantment.getEnchantment().onEntityDamage(stack, thisAs, damage, newDamage, damageType);
					if (enchantDamage != newDamage) {
						finalDamage += enchantDamage - newDamage;
					}
				}
			}
		}
		if (finalDamage != newDamage) {
			super.damageEntity(finalDamage, damageType);
			ci.cancel();
		}
	}
}
