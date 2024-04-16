package turing.enchantmentlib.api;

public class Enchantment implements IEnchant {
	protected String name;
	protected String modid;
	public boolean hidden = false;
	public int maxLevel = 255;

	@Override
	public int getMaxLevel() {
		return maxLevel;
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
