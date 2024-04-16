package turing.enchantmentlib;

import com.mojang.nbt.CompoundTag;
import com.mojang.nbt.Tag;
import net.fabricmc.api.ModInitializer;
import net.minecraft.core.item.ItemArmor;
import net.minecraft.core.item.ItemBow;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.item.tool.*;
import org.jetbrains.annotations.Contract;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import turing.enchantmentlib.api.*;
import turing.enchantmentlib.command.DisenchantCommand;
import turing.enchantmentlib.command.EnchantCommand;
import turing.enchantmentlib.enchants.*;
import turniplabs.halplibe.helper.CommandHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EnchantmentLib implements ModInitializer {
    public static final String MOD_ID = "enchantmentlib";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static final Map<String, IEnchant> ENCHANTMENTS = new HashMap<>();

	public static final IEnchantFilter ANY = stack -> true;
	public static final IEnchantFilter ARMOR = stack -> stack.getItem() instanceof ItemArmor;
	public static final IEnchantFilter TOOL = stack -> stack.getItem() instanceof ItemTool || stack.getItem() instanceof ItemToolSword;
	public static final IEnchantFilter DAMAGEABLE = ItemStack::isItemStackDamageable;
	public static final IEnchantFilter WEAPON = stack -> stack.getItem() instanceof ItemToolSword;
	public static final IEnchantFilter BOW = stack -> stack.getItem() instanceof ItemBow;
	public static final IEnchantFilter DIGGER = stack -> stack.getItem() instanceof ItemToolShovel || stack.getItem() instanceof ItemToolPickaxe || stack.getItem() instanceof ItemToolAxe;
	public static final IEnchantFilter HELM = stack -> ARMOR.canApplyToItem(stack) && ((ItemArmor) stack.getItem()).armorPiece == 0;
	public static final IEnchantFilter CHEST = stack -> ARMOR.canApplyToItem(stack) && ((ItemArmor) stack.getItem()).armorPiece == 1;
	public static final IEnchantFilter LEGS = stack -> ARMOR.canApplyToItem(stack) && ((ItemArmor) stack.getItem()).armorPiece == 2;
	public static final IEnchantFilter BOOTS = stack -> ARMOR.canApplyToItem(stack) && ((ItemArmor) stack.getItem()).armorPiece == 3;

	public static IEnchant SILK_TOUCH;

    @Override
    public void onInitialize() {
		CommandHelper.Core.createCommand(new EnchantCommand());
		CommandHelper.Core.createCommand(new DisenchantCommand());
		initDefaultEnchants();
        LOGGER.info("Enchantment Lib initialized.");
    }

	public static void initDefaultEnchants() {
		new EnchantmentBuilder<>(MOD_ID, new EnchantmentUnbreaking())
			.name("unbreaking")
			.maxLevel(3)
			.build();
		new EnchantmentBuilder<>(MOD_ID, new EnchantmentSharpness())
			.name("sharpness")
			.rarity(2)
			.maxLevel(5)
			.build();
		new EnchantmentBuilder<>(MOD_ID, new EnchantmentLooting())
			.name("looting")
			.rarity(7)
			.maxLevel(3)
			.build();
		new EnchantmentBuilder<>(MOD_ID, new EnchantmentFortune())
			.name("fortune")
			.rarity(8)
			.maxLevel(3)
			.build();
		new EnchantmentBuilder<>(MOD_ID, new EnchantmentInfinity())
			.name("infinity")
			.rarity(17)
			.maxLevel(1)
			.build();
		new EnchantmentBuilder<>(MOD_ID, new EnchantmentMidas())
			.name("midas")
			.rarity(15)
			.maxLevel(1)
			.build();
		new EnchantmentBuilder<>(MOD_ID, new EnchantmentFlame())
			.name("flame")
			.rarity(10)
			.maxLevel(1)
			.build();
		new EnchantmentBuilder<>(MOD_ID, new EnchantmentPower())
			.name("power")
			.rarity(3)
			.maxLevel(3)
			.build();
		new EnchantmentBuilder<>(MOD_ID, new EnchantmentAutosmelt())
			.name("autosmelt")
			.rarity(5)
			.maxLevel(1)
			.build();
		new EnchantmentBuilder<>(MOD_ID, new EnchantmentEfficiency())
			.name("efficiency")
			.rarity(2)
			.maxLevel(5)
			.build();
		SILK_TOUCH = new EnchantmentBuilder<>(MOD_ID, new EnchantmentSilkTouch())
			.name("silk_touch")
			.rarity(4)
			.maxLevel(1)
			.build();
	}

	public static boolean canEnchantItemWith(ItemStack stack, IEnchant enchant, int level) {
		if (!enchant.canApplyToItem(stack)) return false;
		for (EnchantmentData enchantment : getEnchantsForItem(stack)) {
			if (enchantment.getEnchantment().getNamespace().equals(enchant.getNamespace())) {
				if ((enchantment.level + level) > enchant.getMaxLevel()) return false;
				else break;
			}
		}
		return true;
	}

	public static boolean canEnchantItemWith(ItemStack stack, EnchantmentData enchantment) {
		return canEnchantItemWith(stack, enchantment.getEnchantment(), enchantment.level);
	}

	public static boolean enchantItem(ItemStack stack, EnchantmentData enchantment) {
		if (canEnchantItemWith(stack, enchantment)) {
			CompoundTag tag = stack.getData();
			CompoundTag enchants = tag.getCompound("enchantments");
			CompoundTag hidden = enchants.getCompound("hidden");
			if (enchants.containsKey(enchantment.getEnchantment().getNamespace())) {
				int level = enchants.getInteger(enchantment.getEnchantment().getNamespace());
				enchants.putInt(enchantment.getEnchantment().getNamespace(), level + enchantment.level);
			} else {
				enchants.putInt(enchantment.getEnchantment().getNamespace(), enchantment.level);
			}
			if (enchantment.hidden) {
				hidden.putBoolean(enchantment.getEnchantment().getNamespace(), true);
			}
			if (!hidden.getValues().isEmpty()) enchants.put("hidden", hidden);
			tag.put("enchantments", enchants);
			stack.setData(tag);
			return true;
		}
		return false;
	}

	public static boolean enchantItem(ItemStack stack, IEnchant enchant, int level, boolean hidden) {
		EnchantmentData data = new EnchantmentData(enchant, level);
		data.hidden = hidden;
		return enchantItem(stack, data);
	}

	public static boolean enchantItem(ItemStack stack, IEnchant enchant, int level) {
		return enchantItem(stack, enchant, level, false);
	}

	public static boolean removeEnchantFromItem(ItemStack stack, IEnchant enchant, int levelsToTake) {
		if (stack == null || stack.getItem() == null) return false;
		CompoundTag tag = stack.getData();
		CompoundTag enchants = tag.getCompound("enchantments");
		CompoundTag hidden = enchants.getCompound("hidden");
		if (enchants.containsKey(enchant.getNamespace())) {
			if (hidden.containsKey(enchant.getNamespace())) {
				hidden = tagWithoutThisValue(hidden, enchant.getNamespace());
			}

			int current = enchants.getInteger(enchant.getNamespace());
			if (levelsToTake == -1) levelsToTake = current;
			int result = Math.max(current - levelsToTake, 0);
			if (result > 0) {
				enchants.putInt(enchant.getNamespace(), result);
			} else {
				enchants = tagWithoutThisValue(enchants, enchant.getNamespace());
			}

			if (!hidden.getValues().isEmpty()) {
				enchants.put("hidden", hidden);
			} else {
				enchants = tagWithoutThisValue(enchants, "hidden");
			}

			if (!enchants.getValues().isEmpty()) {
				tag.put("enchantments", enchants);
			} else {
				tag = tagWithoutThisValue(tag, "enchantments");
			}

			stack.setData(tag);
			return true;
		}
		return false;
	}

	public static boolean removeEnchantFromItem(ItemStack stack, IEnchant enchant) {
		return removeEnchantFromItem(stack, enchant, -1);
	}

	public static boolean disenchantItem(ItemStack stack) {
		if (stack == null || stack.getItem() == null) return false;
		CompoundTag tag = stack.getData();
		if (tag.containsKey("enchantments")) {
			tag = tagWithoutThisValue(tag, "enchantments");
			stack.setData(tag);
			return true;
		}
		return false;
	}

	public static EnchantmentData[] getEnchantsForItem(ItemStack stack) {
		CompoundTag tag = stack.getData();
		if (tag.containsKey("enchantments")) {
			CompoundTag enchants = tag.getCompound("enchantments");
			CompoundTag hidden = enchants.getCompound("hidden");
			List<EnchantmentData> enchantments = new ArrayList<>();
			for (Tag<?> value : enchants.getValues()) {
				String name = value.getTagName();
				Object v = value.getValue();
				if (v instanceof Integer) {
					int level = (int) v;
					if (level > 0) {
						IEnchant enchantment = ENCHANTMENTS.get(name);
						if (enchantment != null) {
							EnchantmentData data = new EnchantmentData(enchantment, level);
							if (hidden.containsKey(name) && hidden.getBoolean(name)) data.hidden = true;
							enchantments.add(data);
						}
					}
				}
			}
			return enchantments.toArray(new EnchantmentData[0]);
		}
		return new EnchantmentData[0];
	}

	public static int getEnchantmentLevelForItem(ItemStack stack, IEnchant enchant) {
		CompoundTag tag = stack.getData();
		CompoundTag enchants = tag.getCompound("enchantments");
		return enchants.getIntegerOrDefault(enchant.getNamespace(), 0);
	}

	public static boolean isItemEnchanted(ItemStack stack, IEnchant enchantment, int minLevel, int maxLevel) {
		if (stack == null || stack.getItem() == null || stack.stackSize < 1) return false;
		CompoundTag tag = stack.getData();
		if (tag.containsKey("enchantments")) {
			CompoundTag enchants = tag.getCompound("enchantments");
			if (enchants.containsKey(enchantment.getNamespace())) {
				int level = enchants.getInteger(enchantment.getNamespace());
				return level >= minLevel && level <= maxLevel;
			}
		}
		return false;
	}

	public static boolean isItemEnchanted(ItemStack stack, IEnchant enchantment, int level) {
		return isItemEnchanted(stack, enchantment, level, level);
	}

	public static boolean isItemEnchanted(ItemStack stack, IEnchant enchantment) {
		return isItemEnchanted(stack, enchantment, 1, 255);
	}

	public static boolean isItemEnchanted(ItemStack stack) {
		CompoundTag tag = stack.getData();
		return tag.containsKey("enchantments");
	}

	@SuppressWarnings({"api", "UnstableApiUsage"})
	@Contract(mutates = "param1")
	protected static CompoundTag tagWithoutThisValue(CompoundTag tag, String name) {
		Map<String, Tag<?>> map = tag.getValue();
		map.remove(name);
		tag.setValue(map);
		return tag;
	}
}
