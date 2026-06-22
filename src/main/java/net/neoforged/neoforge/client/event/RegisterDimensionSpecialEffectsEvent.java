package net.neoforged.neoforge.client.event;
import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
public class RegisterDimensionSpecialEffectsEvent {
    public void register(ResourceKey<Level> key, DimensionSpecialEffects effects) {}
}