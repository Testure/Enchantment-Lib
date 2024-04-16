package turing.enchantmentlib.api;

import net.minecraft.core.item.ItemStack;

@FunctionalInterface
public interface IEnchantFilter {
	boolean canApplyToItem(ItemStack stack);
}
