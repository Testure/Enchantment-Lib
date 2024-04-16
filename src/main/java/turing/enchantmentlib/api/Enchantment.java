package turing.enchantmentlib.api;

public class Enchantment implements IEnchant {
	protected String name;
	protected String modid;
	public boolean hidden = false;
	public int maxLevel = 255;
	public int rarity = 1;

	@Override
	public int getMaxLevel() {
		return maxLevel;
	}

	@Override
	public int getRarity() {
		return rarity;
	}

	@Override
	public String getModId() {
		return modid;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public boolean isHidden() {
		return hidden;
	}
}
