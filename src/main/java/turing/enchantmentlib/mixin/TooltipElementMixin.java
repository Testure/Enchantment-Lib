package turing.enchantmentlib.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.gui.TooltipElement;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.lang.I18n;
import net.minecraft.core.net.command.TextFormatting;
import net.minecraft.core.player.inventory.slot.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import turing.enchantmentlib.EnchantmentLib;
import turing.enchantmentlib.api.EnchantmentData;

@Mixin(value = TooltipElement.class, remap = false)
public class TooltipElementMixin {
	@Inject(method = "getTooltipText(Lnet/minecraft/core/item/ItemStack;ZLnet/minecraft/core/player/inventory/slot/Slot;)Ljava/lang/String;", at = @At(value = "TAIL", shift = At.Shift.BEFORE))
	public void addEnchantmentDesc(ItemStack stack, boolean showDescription, Slot slot, CallbackInfoReturnable<String> cir, @Local StringBuilder text) {
		for (EnchantmentData enchantment : EnchantmentLib.getEnchantsForItem(stack)) {
			if (!enchantment.getEnchantment().isHidden() && !enchantment.hidden) {
				boolean noLevel = enchantment.level == 1 && enchantment.getEnchantment().getMaxLevel() <= 1;
				text.append("\n");
				text.append(TextFormatting.formatted(noLevel ? enchantment.getEnchantment().getTranslatedName() : I18n.getInstance().translateKeyAndFormat("tooltip.enchantmentlib.enchant", enchantment.getEnchantment().getTranslatedName(), enchantment.level), TextFormatting.PURPLE));
			}
		}
	}
}
