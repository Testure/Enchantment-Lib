package turing.enchantmentlib.enchants;

import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.world.World;
import turing.enchantmentlib.EnchantmentLib;
import turing.enchantmentlib.api.Enchantment;
import turing.enchantmentlib.api.IEnchantFilter;

public class EnchantmentInfinity extends Enchantment {
	@Override
	public boolean doesHaveAmmo(ItemStack stack, Player player, World world) {
		return true;
	}

	@Override
	public boolean shouldCollectArrows(ItemStack stack, Player player, World world) {
		return false;
	}

	@Override
	public IEnchantFilter[] getApplicableFilters() {
		return new IEnchantFilter[]{EnchantmentLib.BOW};
	}
}
