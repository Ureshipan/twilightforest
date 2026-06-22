package twilightforest.mixin;

import net.minecraft.server.level.ServerEntity;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import twilightforest.ASMHooks;

@Mixin(ServerEntity.class)
public abstract class ServerEntityMixin {

	@Shadow private Entity entity;

	/**
	 * Before reading {@code this.entity} in sendDirtyEntityData, route through
	 * ASMHooks so multipart entities also have their dirty data dispatched.
	 */
	@Redirect(
		method = "sendDirtyEntityData",
		at = @At(
			value = "FIELD",
			target = "Lnet/minecraft/server/level/ServerEntity;entity:Lnet/minecraft/world/entity/Entity;",
			ordinal = 0
		)
	)
	private Entity redirectEntityField(ServerEntity self) {
		return ASMHooks.sendDirtyEntityData(this.entity);
	}
}
