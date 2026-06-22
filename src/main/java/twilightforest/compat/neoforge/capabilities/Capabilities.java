package twilightforest.compat.neoforge.capabilities;

import net.minecraft.core.Direction;
import twilightforest.compat.neoforge.items.IItemHandler;

public class Capabilities {
    public static class ItemHandler {
        public static final BlockCapability<IItemHandler, Direction> BLOCK =
            BlockCapability.createSided("neoforge:item_handler", IItemHandler.class, Direction.class);
        public static final BlockCapability<IItemHandler, Direction> ENTITY_AUTOMATION =
            BlockCapability.createSided("neoforge:item_handler_automation", IItemHandler.class, Direction.class);
    }
}
