package turing.enchantmentlib.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.entity.EntityItem;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.enums.EnumDropCause;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import turing.enchantmentlib.EnchantmentLib;
import turing.enchantmentlib.api.EnchantmentData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Mixin(value = Block.class, remap = false)
public abstract class BlockMixin {
	@Shadow
	public int dropOverride;

	@Shadow
	public abstract ItemStack[] getBreakResult(World world, EnumDropCause dropCause, int x, int y, int z, int meta, TileEntity tileEntity);

	@Unique
	public final Block thisAs = (Block)(Object)this;

	@Inject(method = "harvestBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/player/inventory/InventoryPlayer;getCurrentItem()Lnet/minecraft/core/item/ItemStack;", shift = At.Shift.BEFORE), cancellable = true)
	public void changeDrops(World world, EntityPlayer player, int x, int y, int z, int meta, TileEntity tileEntity, CallbackInfo ci) {
		ItemStack stack = player.inventory.getCurrentItem();
		if (EnchantmentLib.isItemEnchanted(stack)) {
			List<ItemStack> drops = new ArrayList<>();
			EnumDropCause cause = EnumDropCause.IMPROPER_TOOL;
			if (player.canHarvestBlock(thisAs)) {
				cause = (stack.getItem().isSilkTouch() || EnchantmentLib.isItemEnchanted(stack, EnchantmentLib.SILK_TOUCH)) ? EnumDropCause.SILK_TOUCH : EnumDropCause.PROPER_TOOL;
			}
			if (dropOverride > 0) {
				drops.add(Item.itemsList[dropOverride].getDefaultStack());
			} else {
				ItemStack[] breakResult = getBreakResult(world, cause, x, y, z, meta, tileEntity);
				if (breakResult != null && breakResult.length > 0) drops.addAll(Arrays.asList(breakResult));
			}

			for (EnchantmentData enchantment : EnchantmentLib.getEnchantsForItem(stack)) {
				drops = enchantment.getEnchantment().getBlockDrops(stack, world, cause, thisAs, x, y, z, meta, player, drops);
			}

			for (ItemStack drop : drops) {
				if (EntityItem.enableItemClumping) {
					world.dropItem(x, y, z, drop.copy());
				} else {
					for (int i = 0; i < drop.stackSize; i++) {
						ItemStack copy = drop.copy();
						copy.stackSize = 1;
						world.dropItem(x, y, z, copy);
					}
				}
			}

			ci.cancel();
		}
	}
}
