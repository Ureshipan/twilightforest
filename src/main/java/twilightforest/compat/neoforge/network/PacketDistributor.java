package twilightforest.compat.neoforge.network;

import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ChunkPos;

public class PacketDistributor {

	public static void sendToPlayersTrackingEntity(Entity entity, CustomPacketPayload packet) {
		PlayerLookup.tracking(entity).forEach(p -> ServerPlayNetworking.send(p, packet));
	}

	public static void sendToPlayersTrackingEntityAndSelf(Entity entity, CustomPacketPayload packet) {
		PlayerLookup.tracking(entity).forEach(p -> ServerPlayNetworking.send(p, packet));
		if (entity instanceof ServerPlayer sp) ServerPlayNetworking.send(sp, packet);
	}

	public static void sendToPlayer(ServerPlayer player, CustomPacketPayload packet) {
		ServerPlayNetworking.send(player, packet);
	}

	public static void sendToAllPlayers(ServerLevel level, CustomPacketPayload packet) {
		PlayerLookup.all(level.getServer()).forEach(p -> ServerPlayNetworking.send(p, packet));
	}

	public static void sendToPlayersTrackingChunk(ServerLevel level, ChunkPos pos, CustomPacketPayload packet) {
		PlayerLookup.tracking(level, pos).forEach(p -> ServerPlayNetworking.send(p, packet));
	}
}
