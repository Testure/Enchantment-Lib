package turing.enchantmentlib.enchants;

import net.minecraft.core.entity.player.Player;
import net.minecraft.core.entity.projectile.ProjectileArrow;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.world.World;
import turing.enchantmentlib.EnchantmentLib;
import turing.enchantmentlib.api.Enchantment;
import turing.enchantmentlib.api.IEnchantFilter;

public class EnchantmentPower extends Enchantment {
	@Override
	public void onArrowFired(ItemStack stack, Player player, World world, ProjectileArrow arrow) {
		arrow.damage += getLevel(stack);
	}

	@Override
	public IEnchantFilter[] getApplicableFilters() {
		return new IEnchantFilter[]{EnchantmentLib.BOW};
	}
}
