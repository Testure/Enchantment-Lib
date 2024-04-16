package turing.enchantmentlib.mixin;

import net.minecraft.core.entity.EntityLiving;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(value = EntityLiving.class, remap = false)
public interface EntityLivingAccessor {
	@Invoker
	int invokeGetDropItemId();
}
