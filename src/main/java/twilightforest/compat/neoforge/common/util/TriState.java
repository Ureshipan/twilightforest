package twilightforest.compat.neoforge.common.util;

public enum TriState {
    TRUE, FALSE, DEFAULT;

    public boolean isTrue() { return this == TRUE; }
    public boolean isFalse() { return this == FALSE; }
    public boolean isDefault() { return this == DEFAULT; }
}
