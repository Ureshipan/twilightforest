package net.neoforged.neoforge.client.event;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.entity.state.PlayerRenderState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntityType;
import java.util.Collection;
import java.util.Set;
import java.util.function.Supplier;
public class EntityRenderersEvent {
    public static class RegisterRenderers extends EntityRenderersEvent {
        public <T extends net.minecraft.world.entity.Entity> void registerEntityRenderer(EntityType<T> type, EntityRendererProvider<T> provider) {}
        public <T extends net.minecraft.world.level.block.entity.BlockEntity> void registerBlockEntityRenderer(BlockEntityType<T> type, BlockEntityRendererProvider<T> provider) {}
    }
    public static class RegisterLayerDefinitions extends EntityRenderersEvent {
        public void registerLayerDefinition(ModelLayerLocation loc, Supplier<LayerDefinition> supplier) {}
    }
    public static class AddLayers extends EntityRenderersEvent {
        private final EntityRendererProvider.Context context;
        public AddLayers(EntityRendererProvider.Context context) { this.context = context; }
        public EntityRendererProvider.Context getContext() { return context; }
        @SuppressWarnings("all")
        public Collection<EntityType<?>> getEntityTypes() { return java.util.List.of(); }
        @SuppressWarnings("all")
        public <T extends net.minecraft.world.entity.Entity> net.minecraft.client.renderer.entity.EntityRenderer<T, ?> getRenderer(EntityType<T> type) { return null; }
        public Set<String> getSkins() { return java.util.Set.of(); }
        @SuppressWarnings("all")
        public <T extends LivingEntity, S extends LivingEntityRenderState, M extends EntityModel<S>> LivingEntityRenderer<T, S, M> getSkin(String skinName) { return null; }
    }
}