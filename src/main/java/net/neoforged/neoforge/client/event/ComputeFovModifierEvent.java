package net.neoforged.neoforge.client.event;
import net.minecraft.world.entity.player.Player;
public class ComputeFovModifierEvent {
    private Player player;
    private float fovModifier;
    public ComputeFovModifierEvent(Player player, float fovModifier) { this.player = player; this.fovModifier = fovModifier; }
    public Player getPlayer() { return player; }
    public float getFovModifier() { return fovModifier; }
    public void setNewFovModifier(float modifier) { this.fovModifier = modifier; }
}