package turing.enchantmentlib.enchants;

import net.minecraft.core.item.ItemStack;
import turing.enchantmentlib.EnchantmentLib;
import turing.enchantmentlib.api.Enchantment;
import turing.enchantmentlib.api.IEnchantFilter;
import turing.enchantmentlib.mixin.ItemAccessor;

public class EnchantmentUnbreaking extends Enchantment {
	@Override
	public int onItemDamage(ItemStack stack, int damageToTake) {
	    if (getLevel(stack) >= 20) return 0;
		if (ItemAccessor.getItemRand().nextInt(20 / getLevel(stack)) == 0) damageToTake = Math.max(damageToTake - 1, 0);
		return damageToTake;
	}

	@Override
	public IEnchantFilter[] getApplicableFilters() {
		return new IEnchantFilter[]{EnchantmentLib.DAMAGEABLE};
	}
}
