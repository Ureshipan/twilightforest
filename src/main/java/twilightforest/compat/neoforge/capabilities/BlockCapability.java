package twilightforest.compat.neoforge.capabilities;

public class BlockCapability<R, C> {
    private final String name;

    private BlockCapability(String name) {
        this.name = name;
    }

    public static <R, C> BlockCapability<R, C> createSided(String name, Class<R> type, Class<C> contextType) {
        return new BlockCapability<>(name);
    }

    public String name() {
        return name;
    }
}
