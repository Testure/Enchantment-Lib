package turing.enchantmentlib.enchants;

import net.minecraft.core.block.Block;
import net.minecraft.core.item.ItemStack;
import turing.enchantmentlib.EnchantmentLib;
import turing.enchantmentlib.api.Enchantment;
import turing.enchantmentlib.api.IEnchantFilter;

public class EnchantmentEfficiency extends Enchantment {
	@Override
	public float getMiningStrength(ItemStack stack, Block<?> block, float strength) {
		return strength + (0.25F * getLevel(stack));
	}

	@Override
	public IEnchantFilter[] getApplicableFilters() {
		return new IEnchantFilter[]{EnchantmentLib.DIGGER};
	}
}
