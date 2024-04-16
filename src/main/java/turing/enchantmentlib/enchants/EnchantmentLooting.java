package turing.enchantmentlib.enchants;

import net.minecraft.core.entity.EntityLiving;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.util.helper.MathHelper;
import turing.enchantmentlib.EnchantmentLib;
import turing.enchantmentlib.api.Enchantment;
import turing.enchantmentlib.api.IEnchantFilter;
import turing.enchantmentlib.mixin.EntityAccessor;
import turing.enchantmentlib.mixin.EntityLivingAccessor;

import java.util.Random;

public class EnchantmentLooting extends Enchantment {
	@Override
	public void onHitEntity(ItemStack stack, EntityPlayer player, EntityLiving entity, boolean didHit) {
		if (!entity.isAlive()) {
			int id = ((EntityLivingAccessor) entity).invokeGetDropItemId();
			if (id > 0) {
				Random rand = ((EntityAccessor) entity).getRandom();
				int count = rand.nextInt(MathHelper.floor_float(1.75F * getLevel(stack)));
				if (count > 0) {
					entity.spawnAtLocation(id, count);
				}
			}
		}
	}

	@Override
	public IEnchantFilter[] getApplicableFilters() {
		return new IEnchantFilter[]{EnchantmentLib.WEAPON};
	}
}
