package net.neoforged.neoforge.client.event;
import net.minecraft.client.Camera;
import net.minecraft.util.profiling.Profiler;
import net.minecraft.world.phys.Vec3;
public class RenderLevelStageEvent {
    public enum Stage {
        AFTER_SKY, AFTER_SOLID_BLOCKS, AFTER_CUTOUT_MIPPED_BLOCKS_BLOCKS, AFTER_CUTOUT_BLOCKS,
        AFTER_ENTITIES, AFTER_BLOCK_ENTITIES, AFTER_TRANSLUCENT_BLOCKS, AFTER_TRIPWIRE_BLOCKS,
        AFTER_PARTICLES, AFTER_WEATHER, AFTER_LEVEL, LAST
    }
    private final Stage stage;
    private final Camera camera;
    public RenderLevelStageEvent(Stage stage, Camera camera) { this.stage = stage; this.camera = camera; }
    public Stage getStage() { return stage; }
    public Camera getCamera() { return camera; }
    public net.minecraft.util.DeltaTracker getPartialTick() { return net.minecraft.util.DeltaTracker.ZERO; }
}