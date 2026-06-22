package net.neoforged.neoforge.client.event;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.Identifier;
import java.util.function.BiConsumer;
public class RegisterGuiLayersEvent {
    public void registerAbove(Identifier existing, Identifier name, BiConsumer<GuiGraphics, Float> layer) {}
    public void registerAboveAll(Identifier name, BiConsumer<GuiGraphics, Float> layer) {}
    public void registerBelow(Identifier existing, Identifier name, BiConsumer<GuiGraphics, Float> layer) {}
    public void registerBelowAll(Identifier name, BiConsumer<GuiGraphics, Float> layer) {}
}