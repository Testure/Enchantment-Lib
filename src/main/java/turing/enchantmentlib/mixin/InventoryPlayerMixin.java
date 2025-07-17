package turing.enchantmentlib.mixin;

import net.minecraft.core.item.ItemArmor;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.item.material.ArmorMaterial;
import net.minecraft.core.player.inventory.container.ContainerInventory;
import net.minecraft.core.util.helper.DamageType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import turing.enchantmentlib.EnchantmentLib;
import turing.enchantmentlib.api.EnchantmentData;

@Mixin(value = ContainerInventory.class, remap = false)
public class InventoryPlayerMixin {
	@Shadow
	public ItemStack[] armorInventory;

	@Inject(method = "getTotalProtectionAmount", at = @At(value = "RETURN"), cancellable = true)
	public void changeProtection(DamageType damageType, CallbackInfoReturnable<Float> cir) {
		float finalProtection = cir.getReturnValue();
		for (ItemStack stack : armorInventory) {
			if (stack != null && stack.getItem() instanceof ItemArmor) {
				ItemArmor armor = (ItemArmor) stack.getItem();
				ArmorMaterial material = armor.getArmorMaterial();
				if (material != null) {
					float protection = material.getProtection(damageType) * armor.getArmorPieceProtectionPercentage();
					for (EnchantmentData enchantment : EnchantmentLib.getEnchantsForItem(stack)) {
						float newProtection = enchantment.getEnchantment().getProtection(stack, damageType, protection);
						if (newProtection != protection) {
							finalProtection += (newProtection - protection);
						}
					}
				}
			}
		}
		cir.setReturnValue(finalProtection);
	}
}
