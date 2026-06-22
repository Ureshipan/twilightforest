package net.neoforged.neoforge.common;
import net.neoforged.bus.api.IEventBus;
import java.util.function.Consumer;
public class NeoForge {
    public static final IEventBus EVENT_BUS = new IEventBus() {
        @Override public <T> void addListener(Consumer<T> listener) {}
        @Override public <T> void addListener(Class<T> eventType, Consumer<T> listener) {}
    };
}