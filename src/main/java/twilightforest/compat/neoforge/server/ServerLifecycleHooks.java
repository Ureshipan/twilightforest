package twilightforest.compat.neoforge.server;

import net.minecraft.server.MinecraftServer;
import org.jetbrains.annotations.Nullable;

public class ServerLifecycleHooks {

	@Nullable
	private static MinecraftServer currentServer;

	public static void setCurrentServer(@Nullable MinecraftServer server) {
		currentServer = server;
	}

	@Nullable
	public static MinecraftServer getCurrentServer() {
		return currentServer;
	}
}
