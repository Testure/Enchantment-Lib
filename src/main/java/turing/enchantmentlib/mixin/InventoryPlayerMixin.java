package turing.enchantmentlib.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.item.ItemArmor;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.InventoryPlayer;
import net.minecraft.core.util.helper.DamageType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import turing.enchantmentlib.EnchantmentLib;
import turing.enchantmentlib.api.EnchantmentData;

@Mixin(value = InventoryPlayer.class, remap = false)
public class InventoryPlayerMixin {
	@Shadow
	public ItemStack[] armorInventory;

	@Inject(method = "getTotalProtectionAmount", at = @At(value = "RETURN", shift = At.Shift.BEFORE), cancellable = true)
	public void changeProtection(DamageType damageType, CallbackInfoReturnable<Float> cir, @Local(name = "protectionPercentage") float totalProtection) {
		float finalProtection = totalProtection;
		for (ItemStack stack : armorInventory) {
			if (stack != null && stack.getItem() instanceof ItemArmor) {
				ItemArmor armor = (ItemArmor) stack.getItem();
				float protection = armor.material.getProtection(damageType) * armor.getArmorPieceProtectionPercentage();
				for (EnchantmentData enchantment : EnchantmentLib.getEnchantsForItem(stack)) {
					float newProtection = enchantment.getEnchantment().getProtection(stack, damageType, protection);
					if (newProtection != protection) {
						finalProtection += (newProtection - protection);
					}
				}
			}
		}
		cir.setReturnValue(finalProtection);
	}
}
