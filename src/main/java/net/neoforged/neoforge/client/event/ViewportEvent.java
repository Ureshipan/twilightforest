package net.neoforged.neoforge.client.event;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.world.level.material.FogType;
public class ViewportEvent {
    public static class ComputeCameraAngles extends ViewportEvent {
        private float yaw, pitch, roll;
        private final float partialTick;
        public ComputeCameraAngles(float partialTick, float yaw, float pitch, float roll) {
            this.partialTick = partialTick; this.yaw = yaw; this.pitch = pitch; this.roll = roll;
        }
        public float getPartialTick() { return partialTick; }
        public float getYaw() { return yaw; } public void setYaw(float yaw) { this.yaw = yaw; }
        public float getPitch() { return pitch; } public void setPitch(float pitch) { this.pitch = pitch; }
        public float getRoll() { return roll; } public void setRoll(float roll) { this.roll = roll; }
    }
    public static class RenderFog extends ViewportEvent {
        private boolean canceled;
        private FogType type;
        private FogRenderer.FogMode mode;
        private float near, far;
        public RenderFog(FogType type, FogRenderer.FogMode mode, float near, float far) {
            this.type = type; this.mode = mode; this.near = near; this.far = far;
        }
        public FogType getType() { return type; }
        public FogRenderer.FogMode getMode() { return mode; }
        public float getNearPlaneDistance() { return near; }
        public void setNearPlaneDistance(float near) { this.near = near; }
        public float getFarPlaneDistance() { return far; }
        public void setFarPlaneDistance(float far) { this.far = far; }
        public void setCanceled(boolean canceled) { this.canceled = canceled; }
        public boolean isCanceled() { return canceled; }
    }
    public static class ComputeFogColor extends ViewportEvent {}
}