package net.neoforged.neoforge.client.renderstate;
import com.google.common.reflect.TypeToken;
import java.util.function.BiConsumer;
public class RegisterRenderStateModifiersEvent {
    @SuppressWarnings("all")
    public void registerEntityModifier(TypeToken<?> rendererType, BiConsumer<?, ?> modifier) {}
    @SuppressWarnings("all")
    public void registerMapModifier(BiConsumer<?, ?> modifier) {}
}