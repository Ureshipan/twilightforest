package twilightforest.mixin.client;

import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import twilightforest.ASMHooks;

import java.util.Iterator;
import java.util.List;

@Mixin(LevelRenderer.class)
public abstract class LevelRendererMixin {

	/**
	 * Redirects the entity iterator used during visible-entity collection so TFPart entities
	 * (multipart boss parts) are also included for rendering, even though they are not in the
	 * main entity list.
	 */
	@Redirect(
		method = "collectVisibleEntities",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/multiplayer/ClientLevel;entitiesForRendering()Ljava/lang/Iterable;"
		)
	)
	private Iterable<Entity> redirectEntitiesForRendering(ClientLevel level) {
		return () -> ASMHooks.resolveEntitiesForRendering(level.entitiesForRendering().iterator());
	}
}
