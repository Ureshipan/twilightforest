package twilightforest.mixin.client;

import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import twilightforest.ASMHooks;

@Mixin(EntityRenderDispatcher.class)
public abstract class EntityRenderDispatcherMixin {

	/**
	 * After the renderer is looked up from the map, let ASMHooks substitute a TFPart-aware
	 * renderer so multipart entity parts can be rendered using custom renderers.
	 */
	@Inject(method = "getRenderer", at = @At("RETURN"), cancellable = true)
	private <T extends Entity> void injectPartEntityRenderer(T entity,
			CallbackInfoReturnable<@Nullable EntityRenderer<?, ?>> cir) {
		EntityRenderer<?, ?> resolved = ASMHooks.resolveEntityRenderer(cir.getReturnValue(), entity);
		if (resolved != cir.getReturnValue()) {
			cir.setReturnValue(resolved);
		}
	}
}
