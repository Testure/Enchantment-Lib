package turing.enchantmentlib.enchants;

import net.minecraft.core.item.ItemStack;
import net.minecraft.core.world.World;
import net.minecraft.core.entity.projectile.EntityFireball;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.util.phys.Vec3d;
import turing.enchantmentlib.EnchantmentLib;
import turing.enchantmentlib.api.Enchantment;
import turing.enchantmentlib.api.IEnchantFilter;

public class EnchantmentGhast extends Enchantment {
	@Override
	public ItemStack onRightClick(ItemStack stack, World world, EntityPlayer player) {
	    Vec3d vec3d = player.getViewVector(1.0F);
	    EntityFireball entityfireball = new EntityFireball(world, player, 0, 0, 0);
        entityfireball.x = player.x + vec3d.xCoord * 2.0d;
        entityfireball.y = player.y + (player.bbHeight / 2.0F);
        entityfireball.z = player.z + vec3d.zCoord * 2.0d;
        world.entityJoinedWorld(entityfireball);
        entityfireball.accelX = 0.0D;
        entityfireball.accelY = 0.0D;
        entityfireball.accelZ = 0.0D;
        stack.damageItem(500, player);
        return stack;
    }

	@Override
	public IEnchantFilter[] getApplicableFilters() {
		return new IEnchantFilter[]{EnchantmentLib.FIRESTRIKER};
	}
}
