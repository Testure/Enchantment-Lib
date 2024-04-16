package turing.enchantmentlib.enchants;

import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.world.World;
import turing.enchantmentlib.EnchantmentLib;
import turing.enchantmentlib.api.Enchantment;
import turing.enchantmentlib.api.IEnchantFilter;

public class EnchantmentInfinity extends Enchantment {
	@Override
	public boolean doesHaveAmmo(ItemStack stack, EntityPlayer player, World world) {
		return true;
	}

	@Override
	public boolean shouldCollectArrows(ItemStack stack, EntityPlayer player, World world) {
		return false;
	}

	@Override
	public IEnchantFilter[] getApplicableFilters() {
		return new IEnchantFilter[]{EnchantmentLib.BOW};
	}
}
