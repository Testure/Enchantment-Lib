package turing.enchantmentlib.api;

public class EnchantmentData {
	protected IEnchant enchantment;
	public int level;
	public boolean hidden = false;

	public EnchantmentData(IEnchant enchantment, int level) {
		this.enchantment = enchantment;
		this.level = Math.min(Math.max(level, 1), 255);
	}

	public EnchantmentData(IEnchant enchantment) {
		this(enchantment, 1);
	}

	public IEnchant getEnchantment() {
		return enchantment;
	}
}
