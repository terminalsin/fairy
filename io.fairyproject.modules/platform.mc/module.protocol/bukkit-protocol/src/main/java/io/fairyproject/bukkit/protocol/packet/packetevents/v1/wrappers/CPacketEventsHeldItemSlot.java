package io.fairyproject.bukkit.protocol.packet.packetevents.v1.wrappers;

import io.fairyproject.mc.protocol.netty.Channel;
import io.fairyproject.mc.protocol.packet.client.CPacketHeldItemSlot;
import io.fairyproject.mc.protocol.spigot.packet.packetevents.PacketEventWrapper;
import io.github.retrooper.packetevents.packetwrappers.play.in.helditemslot.WrappedPacketInHeldItemSlot;

public class CPacketEventsHeldItemSlot extends PacketEventWrapper<WrappedPacketInHeldItemSlot> implements CPacketHeldItemSlot {
    public CPacketEventsHeldItemSlot(WrappedPacketInHeldItemSlot wrapper, Channel channel) {
        super(wrapper, channel);
    }

    @Override
    public int getSlot() {
        return wrapper.getCurrentSelectedSlot();
    }
}
