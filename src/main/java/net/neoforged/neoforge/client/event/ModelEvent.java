package net.neoforged.neoforge.client.event;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.Identifier;
import java.util.HashMap;
import java.util.Map;
public class ModelEvent {
    public record BakingResult(Map<ModelResourceLocation, BakedModel> blockStateModels,
                               Map<Identifier, BakedModel> standaloneModels) {
        public BakingResult() { this(new HashMap<>(), new HashMap<>()); }
    }
    public static class BakingCompleted extends ModelEvent {
        private final BakingResult bakingResult;
        public BakingCompleted(BakingResult result) { this.bakingResult = result; }
        public BakingResult getBakingResult() { return bakingResult; }
    }
    public static class ModifyBakingResult extends ModelEvent {
        private final BakingResult bakingResult;
        public ModifyBakingResult(BakingResult result) { this.bakingResult = result; }
        public BakingResult getBakingResult() { return bakingResult; }
    }
    public static class RegisterAdditional extends ModelEvent {
        public void register(Identifier id) {}
    }
    public static class RegisterLoaders extends ModelEvent {
        public void register(Identifier id, Object loader) {}
    }
}