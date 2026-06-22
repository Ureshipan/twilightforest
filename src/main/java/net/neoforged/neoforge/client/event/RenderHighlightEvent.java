package net.neoforged.neoforge.client.event;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.phys.BlockHitResult;
public class RenderHighlightEvent {
    private boolean canceled;
    private final Camera camera;
    private final PoseStack poseStack;
    private final MultiBufferSource multiBufferSource;
    public RenderHighlightEvent(Camera camera, PoseStack poseStack, MultiBufferSource multiBufferSource) {
        this.camera = camera; this.poseStack = poseStack; this.multiBufferSource = multiBufferSource;
    }
    public Camera getCamera() { return camera; }
    public PoseStack getPoseStack() { return poseStack; }
    public MultiBufferSource getMultiBufferSource() { return multiBufferSource; }
    public void setCanceled(boolean canceled) { this.canceled = canceled; }
    public boolean isCanceled() { return canceled; }
    public static class Block extends RenderHighlightEvent {
        private final BlockHitResult target;
        public Block(BlockHitResult target, Camera camera, PoseStack poseStack, MultiBufferSource multiBufferSource) {
            super(camera, poseStack, multiBufferSource); this.target = target;
        }
        public BlockHitResult getTarget() { return target; }
    }
}