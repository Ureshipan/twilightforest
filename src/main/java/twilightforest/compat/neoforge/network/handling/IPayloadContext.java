package twilightforest.compat.neoforge.network.handling;

import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.world.entity.player.Player;

public interface IPayloadContext {
    void enqueueWork(Runnable task);
    Player player();
    PacketFlow flow();
}
