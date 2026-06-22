package net.neoforged.neoforge.event.level;
import net.minecraft.world.level.LevelAccessor;
public class LevelEvent {
    private final LevelAccessor level;
    public LevelEvent(LevelAccessor level) { this.level = level; }
    public LevelAccessor getLevel() { return level; }
    public static class Unload extends LevelEvent {
        public Unload(LevelAccessor level) { super(level); }
    }
}