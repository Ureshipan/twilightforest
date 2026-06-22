package twilightforest.compat.neoforge.common.util;

import com.mojang.authlib.GameProfile;
import net.minecraft.server.level.ServerLevel;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class FakePlayerFactory {
    private static final GameProfile MINECRAFT_PROFILE = new GameProfile(UUID.fromString("41C82C87-7AfB-4024-BA57-13D2C99CAE77"), "[Minecraft]");

    @Nullable
    public static FakePlayer getMinecraft(ServerLevel level) {
        try {
            return new FakePlayer(level, MINECRAFT_PROFILE);
        } catch (Exception e) {
            return null;
        }
    }
}
