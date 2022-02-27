package io.fairyproject.bukkit.protocol.packet.packetevents.v1.wrappers;

import io.fairyproject.mc.protocol.netty.Channel;
import io.fairyproject.mc.protocol.packet.client.CPacketSpectate;
import io.fairyproject.mc.protocol.spigot.packet.packetevents.PacketEventWrapper;
import io.github.retrooper.packetevents.packetwrappers.play.in.spectate.WrappedPacketInSpectate;

import java.util.UUID;

public class CPacketEventsSpectate extends PacketEventWrapper<WrappedPacketInSpectate> implements CPacketSpectate {
    public CPacketEventsSpectate(WrappedPacketInSpectate wrapper, Channel channel) {
        super(wrapper, channel);
    }

    @Override
    public UUID getSpectated() {
        return wrapper.getUUID();
    }
}
