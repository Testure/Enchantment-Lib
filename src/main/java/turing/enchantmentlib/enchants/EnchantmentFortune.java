package turing.enchantmentlib.enchants;

import net.minecraft.core.block.Block;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.enums.EnumDropCause;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.item.block.ItemBlock;
import net.minecraft.core.util.helper.MathHelper;
import net.minecraft.core.world.World;
import turing.enchantmentlib.EnchantmentLib;
import turing.enchantmentlib.api.Enchantment;
import turing.enchantmentlib.api.IEnchantFilter;

import java.util.List;

public class EnchantmentFortune extends Enchantment {
	@Override
	public List<ItemStack> getBlockDrops(ItemStack stack, World world, EnumDropCause cause, Block block, int x, int y, int z, int meta, EntityPlayer player, List<ItemStack> drops) {
		if (cause == EnumDropCause.PROPER_TOOL) {
			int level = getLevel(stack);
			for (ItemStack drop : drops) {
				if (!(drop.getItem() instanceof ItemBlock)) {
					int count = world.rand.nextInt(MathHelper.floor_float(1.75F * level));
					if (count > 0) {
						drop.stackSize += count;
					}
				}
			}
		}
		return drops;
	}

	@Override
	public IEnchantFilter[] getApplicableFilters() {
		return new IEnchantFilter[]{EnchantmentLib.DIGGER};
	}
}
