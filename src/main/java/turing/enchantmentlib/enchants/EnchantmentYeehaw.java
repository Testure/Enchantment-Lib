package turing.enchantmentlib.enchants;

import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.entity.EntityLiving;
import net.minecraft.core.item.ItemStack;
import turing.enchantmentlib.EnchantmentLib;
import turing.enchantmentlib.api.Enchantment;
import turing.enchantmentlib.api.IEnchantFilter;

public class EnchantmentYeehaw extends Enchantment {
	@Override
	public boolean onUseOnEntity(ItemStack stack, EntityPlayer player, EntityLiving entity, boolean didUse) {
	    player.startRiding(entity);
	    return didUse;
    }

	@Override
	public IEnchantFilter[] getApplicableFilters() {
		return new IEnchantFilter[]{EnchantmentLib.SADDLE};
	}
}
