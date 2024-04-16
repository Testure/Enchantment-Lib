package turing.enchantmentlib.mixin;

import net.minecraft.core.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Random;

@Mixin(value = Entity.class, remap = false)
public interface EntityAccessor {
	@Accessor
	Random getRandom();
}
