package turing.enchantmentlib.command;

import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.net.command.Command;
import net.minecraft.core.net.command.CommandError;
import net.minecraft.core.net.command.CommandHandler;
import net.minecraft.core.net.command.CommandSender;
import turing.enchantmentlib.EnchantmentLib;
import turing.enchantmentlib.api.IEnchant;

public class EnchantCommand extends Command {
	public EnchantCommand() {
		super("enchant");
	}

	@Override
	public boolean execute(CommandHandler handler, CommandSender sender, String[] args) {
		if (args.length < 1 || args.length > 2) return false;
		if (!sender.isPlayer()) return false;

		EntityPlayer player = sender.getPlayer();

		ItemStack stack = player.getHeldItem();
		if (stack == null || stack.getItem() == null) throw new CommandError("No item to enchant");

		String enchantment = args[0];
		IEnchant enchant = EnchantmentLib.ENCHANTMENTS.get(enchantment);
		if (enchant == null) throw new CommandError("Could not find enchantment '" + enchantment + "'");

		int level = 1;
		if (args.length > 1) {
			try {
				level = Integer.parseInt(args[1]);
			} catch (Exception e) {
				throw new CommandError("Invalid level argument");
			}
			if (level < 1 || level > enchant.getMaxLevel()) throw new CommandError("Invalid level argument");
		}

		if (!EnchantmentLib.enchantItem(stack, enchant, level)) throw new CommandError("Could not apply enchantment '" + enchantment + "' to item" + (args.length > 1 ? " at level " + level : ""));

		return true;
	}

	@Override
	public boolean opRequired(String[] strings) {
		return true;
	}

	@Override
	public void sendCommandSyntax(CommandHandler handler, CommandSender sender) {
		sender.sendMessage("/enchant <enchantment>");
		sender.sendMessage("/enchant <enchantment> <level>");
	}
}
