package net.neoforged.neoforge.client.event;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import java.util.function.Function;
public class RegisterMenuScreensEvent {
    @FunctionalInterface
    public interface ScreenConstructor<M extends AbstractContainerMenu, U extends AbstractContainerScreen<M>> {
        U create(M menu, Inventory inventory, Component title);
    }
    public <M extends AbstractContainerMenu, U extends AbstractContainerScreen<M>> void register(MenuType<M> type, ScreenConstructor<M, U> factory) {}
}