package turing.enchantmentlib.enchants;

import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.EntityLiving;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.ItemStack;
import turing.enchantmentlib.EnchantmentLib;
import turing.enchantmentlib.api.Enchantment;
import turing.enchantmentlib.api.IEnchantFilter;

public class EnchantmentKnockback extends Enchantment {
	@Override
	public void onHitEntity(ItemStack stack, EntityPlayer player, EntityLiving entity, boolean didHit) {
	    if (didHit && entity != null) {
			double xd = player.x - entity.x;
			double zd = player.z - entity.z;
			double level = 255;//(double) getLevel(stack);
			xd *= level / 2.0d;
			zd *= level / 2.0d;
			while (xd * xd + zd * zd < 0.0001) {
				xd = (Math.random() - Math.random()) * 0.01d;
				zd = (Math.random() - Math.random()) * 0.01d;
			}
            entity.knockBack(player, 0, xd, zd);
	    }
	}

	@Override
	public IEnchantFilter[] getApplicableFilters() {
		return new IEnchantFilter[]{EnchantmentLib.WEAPON};
	}
}
