package turing.enchantmentlib.enchants;

import net.minecraft.core.entity.EntityLiving;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.util.helper.MathHelper;
import turing.enchantmentlib.EnchantmentLib;
import turing.enchantmentlib.api.Enchantment;
import turing.enchantmentlib.api.IEnchantFilter;
import turing.enchantmentlib.mixin.EntityAccessor;

import java.util.List;
import java.util.Random;

public class EnchantmentLooting extends Enchantment {
	@Override
	public void getExtraEntityDrops(ItemStack stack, EntityLiving entity, List<ItemStack> baseDrops, List<ItemStack> extraDrops) {
		Random rand = ((EntityAccessor) entity).getRandom();
		int level = getLevel(stack);
		if (!baseDrops.isEmpty()) {
			ItemStack baseDrop = baseDrops.get(0);
			int count = rand.nextInt(Math.max(MathHelper.floor_float(level * 1.75F), 2));
			if (count > 0) {
				ItemStack copy = baseDrop.copy();
				copy.stackSize = count;
				extraDrops.add(copy);
			}
		}
	}

	@Override
	public IEnchantFilter[] getApplicableFilters() {
		return new IEnchantFilter[]{EnchantmentLib.WEAPON};
	}
}
