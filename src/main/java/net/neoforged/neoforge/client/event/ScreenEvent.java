package net.neoforged.neoforge.client.event;
import net.minecraft.client.gui.screens.Screen;
public class ScreenEvent {
    private final Screen screen;
    public ScreenEvent(Screen screen) { this.screen = screen; }
    public Screen getScreen() { return screen; }
    public static class Init extends ScreenEvent {
        public Init(Screen screen) { super(screen); }
        public static class Pre extends Init { public Pre(Screen screen) { super(screen); } }
        public static class Post extends Init { public Post(Screen screen) { super(screen); } }
    }
    public static class Closing extends ScreenEvent {
        public Closing(Screen screen) { super(screen); }
    }
}