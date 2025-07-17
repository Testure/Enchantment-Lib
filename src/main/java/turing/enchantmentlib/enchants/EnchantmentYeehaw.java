package turing.enchantmentlib.enchants;


import net.minecraft.core.entity.Mob;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import turing.enchantmentlib.EnchantmentLib;
import turing.enchantmentlib.api.Enchantment;
import turing.enchantmentlib.api.IEnchantFilter;

public class EnchantmentYeehaw extends Enchantment {
	@Override
	public boolean onUseOnEntity(ItemStack stack, Player player, Mob entity, boolean didUse) {
	    player.startRiding(entity);
	    return didUse;
    }

	@Override
	public IEnchantFilter[] getApplicableFilters() {
		return new IEnchantFilter[]{EnchantmentLib.SADDLE};
	}
}
