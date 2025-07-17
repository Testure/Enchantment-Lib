package turing.enchantmentlib.api;

import net.minecraft.core.block.Block;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.Mob;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.entity.projectile.ProjectileArrow;
import net.minecraft.core.enums.EnumDropCause;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.lang.I18n;
import net.minecraft.core.util.helper.DamageType;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import turing.enchantmentlib.EnchantmentLib;

import java.util.List;

public interface IEnchant extends IEnchantFilter {
	String getModId();

	String getName();

	default IEnchantFilter[] getApplicableFilters() {
		return new IEnchantFilter[]{EnchantmentLib.ANY};
	}

	default int getMaxLevel() {
		return 255;
	}

	default int getRarity() {
		return 1;
	}

	default boolean isHidden() {
		return false;
	}

	default String getNamespace() {
		return getModId() + ":" + getName();
	}

	default String getTranslatedName() {
		return I18n.getInstance().translateNameKey("enchantment." + getModId() + "." + getName());
	}

	@Override
	default boolean canApplyToItem(ItemStack stack) {
		for (IEnchantFilter filter : getApplicableFilters()) {
			if (filter.canApplyToItem(stack)) return true;
		}
		return false;
	}

	default int getLevel(ItemStack stack) {
		return EnchantmentLib.getEnchantmentLevelForItem(stack, this);
	}

	default int onItemDamage(ItemStack stack, int damageToTake) {
		return damageToTake;
	}

	default int onRepairItem(ItemStack stack, int damageToRepair) {
		return damageToRepair;
	}

	default boolean beforeDestroyBlock(ItemStack stack, World world, int x, int y, int z, Player player, boolean willDestroy) {
		return willDestroy;
	}

	default boolean onDestroyBlock(ItemStack stack, World world, int id, int x, int y, int z, Player player, boolean didDestroy) {
		return didDestroy;
	}

	default List<ItemStack> getBlockDrops(ItemStack stack, World world, EnumDropCause cause, Block<?> block, int x, int y, int z, int meta, Player player, List<ItemStack> drops) {
		return drops;
	}

	default void getExtraEntityDrops(ItemStack stack, Entity entity, List<ItemStack> baseDrops, List<ItemStack> extraDrops) {

	}

	default boolean canHarvestBlock(ItemStack stack, Block<?> block, boolean canHarvest) {
		return canHarvest;
	}

	default void onHitEntity(ItemStack stack, Player player, Mob entity, boolean didHit) {

	}

	default int getAttackDamage(ItemStack stack, Entity entity, int baseDamage) {
		return baseDamage;
	}

	default boolean onUseOnEntity(ItemStack stack, Player player, Mob entity, boolean didUse) {
		return didUse;
	}

	default ItemStack onRightClick(ItemStack stack, World world, Player player) {
		return stack;
	}

	default boolean onUse(ItemStack stack, World world, Player player, int x, int y, int z, Side side, double xPlaced, double yPlaced, boolean didUse) {
		return didUse;
	}

	default float getMiningStrength(ItemStack stack, Block<?> block, float strength) {
		return strength;
	}

	default float getProtection(ItemStack stack, DamageType damageType, float protection) {
		return protection;
	}

	default int onEntityDamage(ItemStack stack, Mob entity, int damage, int newDamage, DamageType damageType) {
		return damage;
	}

	default boolean doesHaveAmmo(ItemStack stack, Player player, World world) {
		return false;
	}

	default boolean shouldCollectArrows(ItemStack stack, Player player, World world) {
		return true;
	}

	default boolean shouldArrowBeGolden(ItemStack stack, Player player, World world) {
		return false;
	}

	default void onArrowFired(ItemStack stack, Player player, World world, ProjectileArrow arrow) {

	}
}
