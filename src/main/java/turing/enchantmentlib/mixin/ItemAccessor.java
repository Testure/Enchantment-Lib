package turing.enchantmentlib.mixin;

import net.minecraft.core.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Random;

@Mixin(value = Item.class, remap = false)
public interface ItemAccessor {
	@Accessor
	static Random getItemRand() {
		throw new AssertionError();
	}
}
