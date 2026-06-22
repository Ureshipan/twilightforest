package net.neoforged.neoforge.client.event;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
public class RenderLivingEvent<T extends net.minecraft.world.entity.LivingEntity, S extends LivingEntityRenderState, M extends net.minecraft.client.model.EntityModel<S>> {
    private final S renderState;
    private final LivingEntityRenderer<T, S, M> renderer;
    public RenderLivingEvent(S renderState, LivingEntityRenderer<T, S, M> renderer) {
        this.renderState = renderState; this.renderer = renderer;
    }
    public S getRenderState() { return renderState; }
    public LivingEntityRenderer<T, S, M> getRenderer() { return renderer; }
    public static class Pre<T extends net.minecraft.world.entity.LivingEntity, S extends LivingEntityRenderState, M extends net.minecraft.client.model.EntityModel<S>> extends RenderLivingEvent<T, S, M> {
        public Pre(S renderState, LivingEntityRenderer<T, S, M> renderer) { super(renderState, renderer); }
    }
    public static class Post<T extends net.minecraft.world.entity.LivingEntity, S extends LivingEntityRenderState, M extends net.minecraft.client.model.EntityModel<S>> extends RenderLivingEvent<T, S, M> {
        public Post(S renderState, LivingEntityRenderer<T, S, M> renderer) { super(renderState, renderer); }
    }
}