package net.neoforged.neoforge.client.event;
import net.minecraft.client.gui.GuiGraphics;
public class CustomizeGuiOverlayEvent {
    private boolean canceled;
    public void setCanceled(boolean canceled) { this.canceled = canceled; }
    public boolean isCanceled() { return canceled; }
    public static class BossEventProgress extends CustomizeGuiOverlayEvent {
        private final Object bossEvent;
        private final GuiGraphics guiGraphics;
        private final int x, y;
        public BossEventProgress(Object bossEvent, GuiGraphics guiGraphics, int x, int y) {
            this.bossEvent = bossEvent; this.guiGraphics = guiGraphics; this.x = x; this.y = y;
        }
        public Object getBossEvent() { return bossEvent; }
        public GuiGraphics getGuiGraphics() { return guiGraphics; }
        public int getX() { return x; }
        public int getY() { return y; }
    }
}