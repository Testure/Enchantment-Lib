package turing.enchantmentlib.enchants;

import net.minecraft.core.block.Block;
import net.minecraft.core.data.registry.Registries;
import net.minecraft.core.data.registry.recipe.RecipeSymbol;
import net.minecraft.core.data.registry.recipe.entry.RecipeEntryFurnace;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.enums.EnumDropCause;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.world.World;
import turing.enchantmentlib.EnchantmentLib;
import turing.enchantmentlib.api.Enchantment;
import turing.enchantmentlib.api.IEnchantFilter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EnchantmentAutosmelt extends Enchantment {
	@Override
	public List<ItemStack> getBlockDrops(ItemStack stack, World world, EnumDropCause cause, Block block, int x, int y, int z, int meta, EntityPlayer player, List<ItemStack> drops) {
		if (cause == EnumDropCause.PROPER_TOOL) {
			List<ItemStack> newDrops = new ArrayList<>();
			for (ItemStack drop : drops) {
				Optional<RecipeEntryFurnace> furnaceRecipe = Registries.RECIPES.getAllFurnaceRecipes().stream().filter(recipe -> recipe.getInput().matches(drop)).findFirst();
				if (furnaceRecipe.isPresent()) {
					newDrops.add(furnaceRecipe.get().getOutput());
				} else {
					newDrops.add(drop);
				}
			}
			return newDrops;
		}
		return drops;
	}

	@Override
	public IEnchantFilter[] getApplicableFilters() {
		return new IEnchantFilter[]{EnchantmentLib.DIGGER};
	}
}
