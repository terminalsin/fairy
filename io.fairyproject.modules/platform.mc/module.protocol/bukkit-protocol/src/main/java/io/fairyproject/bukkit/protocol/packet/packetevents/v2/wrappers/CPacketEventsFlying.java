package io.fairyproject.bukkit.protocol.packet.packetevents.v2.wrappers;

import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerFlying;
import io.fairyproject.bukkit.protocol.packet.packetevents.v2.PacketEventV2Wrapper;
import io.fairyproject.mc.MCPlayer;
import io.fairyproject.mc.protocol.packet.client.CPacketFlying;

public class CPacketEventsFlying extends PacketEventV2Wrapper<WrapperPlayClientPlayerFlying> implements CPacketFlying {
    public CPacketEventsFlying(WrapperPlayClientPlayerFlying wrapper, MCPlayer channel) {
        super(wrapper, channel);
    }

    @Override
    public boolean isGround() {
        return wrapper.isOnGround();
    }
}
