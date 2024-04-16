package turing.enchantmentlib.api;

import turing.enchantmentlib.EnchantmentLib;

public class EnchantmentBuilder<T extends Enchantment> {
	private final T enchant;
	private int maxLevel;
	private final String modid;
	private String name;
	private boolean hidden;

	public EnchantmentBuilder(String modid, T enchant) {
		this.modid = modid;
		this.enchant = enchant;
	}

	public EnchantmentBuilder<T> name(String name) {
		this.name = name;
		return this;
	}

	public EnchantmentBuilder<T> maxLevel(int maxLevel) {
		this.maxLevel = Math.max(Math.min(maxLevel, 255), 1);
		return this;
	}

	public EnchantmentBuilder<T> hidden(boolean isHidden) {
		this.hidden = isHidden;
		return this;
	}

	public EnchantmentBuilder<T> hidden() {
		return hidden(!this.hidden);
	}

	public T build() {
		if (EnchantmentLib.ENCHANTMENTS.containsKey(modid + ":" + name)) throw new IllegalStateException("Enchantment '" + modid + ":" + name + "' already exists!");
		enchant.modid = modid;
		enchant.name = name;
		enchant.maxLevel = maxLevel;
		enchant.hidden = hidden;
		EnchantmentLib.ENCHANTMENTS.put(enchant.getNamespace(), enchant);
		return enchant;
	}
}
