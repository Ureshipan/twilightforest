package twilightforest.util.multiparts;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.world.entity.Entity;
import twilightforest.compat.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.Nullable;
import twilightforest.client.BakedMultiPartRenderers;
import twilightforest.entity.TFPart;
import twilightforest.network.UpdateTFMultipartPacket;

import java.util.Iterator;

public class MultipartEntityUtil {

	public Iterator<Entity> injectTFPartEntities(Iterator<Entity> iter) {
		return new MultipartEntityIteratorWrapper(iter);
	}

	@Nullable
	public EntityRenderer<?, ?> tryLookupTFPartRenderer(@Nullable EntityRenderer<?, ?> renderer, Entity entity) {
		if (entity instanceof TFPart<?> part)
			return BakedMultiPartRenderers.lookup(part.renderer());
		return renderer;
	}

	public Entity sendDirtyMultipartEntityData(Entity entity) {
		if (entity instanceof nordmods.primitive_multipart_entities.common.entity.MultipartEntity)
			PacketDistributor.sendToPlayersTrackingEntity(entity, new UpdateTFMultipartPacket(entity));
		return entity;
	}

}
