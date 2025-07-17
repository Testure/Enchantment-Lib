package turing.enchantmentlib.mixin;

import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.EntityItem;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

@Mixin(value = Entity.class, remap = false)
public class EntityMixin {
	@Shadow
	public World world;
	@Shadow
	public double x;
	@Shadow
	public double y;
	@Shadow
	public double z;

	@Unique
	public ThreadLocal<List<ItemStack>> dropList = ThreadLocal.withInitial(ArrayList::new);

	@Inject(method = "dropItem(Lnet/minecraft/core/item/ItemStack;F)Lnet/minecraft/core/entity/EntityItem;", at = @At("HEAD"))
	public void getDroppedItems(ItemStack stack, float verticalOffset, CallbackInfoReturnable<EntityItem> cir) {
		dropList.get().add(stack);
	}
}
