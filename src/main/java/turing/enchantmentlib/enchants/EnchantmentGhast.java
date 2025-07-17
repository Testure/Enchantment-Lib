package turing.enchantmentlib.enchants;

import net.minecraft.core.entity.player.Player;
import net.minecraft.core.entity.projectile.ProjectileFireball;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.util.phys.Vec3;
import net.minecraft.core.world.World;
import turing.enchantmentlib.EnchantmentLib;
import turing.enchantmentlib.api.Enchantment;
import turing.enchantmentlib.api.IEnchantFilter;

public class EnchantmentGhast extends Enchantment {
	@Override
	public ItemStack onRightClick(ItemStack stack, World world, Player player) {
	    Vec3 vec3 = player.getViewVector(1.0F);
	    ProjectileFireball entityfireball = new ProjectileFireball(world, player, 0, 0, 0);
        entityfireball.x = player.x + vec3.x * 2.0d;
        entityfireball.y = player.y + (player.bbHeight / 2.0F);
        entityfireball.z = player.z + vec3.z * 2.0d;
        world.entityJoinedWorld(entityfireball);
        entityfireball.xd = 0.0D;
        entityfireball.yd = 0.0D;
        entityfireball.zd = 0.0D;
        stack.damageItem(500, player);
        return stack;
    }

	@Override
	public IEnchantFilter[] getApplicableFilters() {
		return new IEnchantFilter[]{EnchantmentLib.FIRESTRIKER};
	}
}
