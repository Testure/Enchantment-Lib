package turing.enchantmentlib.command;

import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.net.command.Command;
import net.minecraft.core.net.command.CommandError;
import net.minecraft.core.net.command.CommandHandler;
import net.minecraft.core.net.command.CommandSender;
import turing.enchantmentlib.EnchantmentLib;
import turing.enchantmentlib.api.IEnchant;

public class DisenchantCommand extends Command {
	public DisenchantCommand() {
		super("disenchant", "unenchant");
	}

	@Override
	public boolean execute(CommandHandler handler, CommandSender sender, String[] args) {
		if (args.length < 1 || args.length > 2) return false;
		if (!sender.isPlayer()) return false;

		EntityPlayer player = sender.getPlayer();
		ItemStack stack = player.getHeldItem();

		if (stack == null || stack.getItem() == null) throw new CommandError("No item to disenchant");

		String enchantment = args[0];

		if (enchantment.equalsIgnoreCase("all")) {
			if (!EnchantmentLib.disenchantItem(stack)) throw new CommandError("Could not remove enchantments from item");
		} else {
			IEnchant enchant = EnchantmentLib.ENCHANTMENTS.get(enchantment);

			if (enchant == null) throw new CommandError("Could not find enchantment '" + enchantment + "'");

			int level = -1;
			if (args.length > 1) {
				try {
					level = Integer.parseInt(args[1]);
				} catch (Exception e) {
					throw new CommandError("Invalid level argument");
				}
				if ((level < 1 && level != -1)) throw new CommandError("Invalid level argument");
			}

			if (!EnchantmentLib.removeEnchantFromItem(stack, enchant, level)) throw new CommandError("Could not remove " + (level > -1 ? level + " levels of" : "") + " enchantment '" + enchantment + "' from item");
		}

		return true;
	}

	@Override
	public boolean opRequired(String[] strings) {
		return true;
	}

	@Override
	public void sendCommandSyntax(CommandHandler handler, CommandSender sender) {
		sender.sendMessage("/disenchant all");
		sender.sendMessage("/disenchant <enchantment>");
		sender.sendMessage("/disenchant <enchantment> <levels-to-take>");
	}
}
