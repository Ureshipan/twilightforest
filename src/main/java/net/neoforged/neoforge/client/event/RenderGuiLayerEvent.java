package net.neoforged.neoforge.client.event;
import net.minecraft.resources.Identifier;
public class RenderGuiLayerEvent {
    private Identifier name;
    private boolean canceled;
    public RenderGuiLayerEvent(Identifier name) { this.name = name; }
    public Identifier getName() { return name; }
    public void setCanceled(boolean canceled) { this.canceled = canceled; }
    public boolean isCanceled() { return canceled; }
    public static class Pre extends RenderGuiLayerEvent {
        public Pre(Identifier name) { super(name); }
    }
    public static class Post extends RenderGuiLayerEvent {
        public Post(Identifier name) { super(name); }
    }
}