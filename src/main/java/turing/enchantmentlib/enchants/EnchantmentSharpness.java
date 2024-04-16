package turing.enchantmentlib.enchants;

import net.minecraft.core.entity.Entity;
import net.minecraft.core.item.ItemStack;
import turing.enchantmentlib.EnchantmentLib;
import turing.enchantmentlib.api.Enchantment;
import turing.enchantmentlib.api.IEnchantFilter;

public class EnchantmentSharpness extends Enchantment {
	@Override
	public int getAttackDamage(ItemStack stack, Entity entity, int baseDamage) {
		return baseDamage + (int)(1.25F * getLevel(stack));
	}

	@Override
	public IEnchantFilter[] getApplicableFilters() {
		return new IEnchantFilter[]{EnchantmentLib.WEAPON};
	}
}
