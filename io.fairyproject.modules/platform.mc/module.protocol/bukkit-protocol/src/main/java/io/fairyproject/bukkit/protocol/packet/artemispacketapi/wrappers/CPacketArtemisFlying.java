package io.fairyproject.bukkit.protocol.packet.artemispacketapi.wrappers;

import io.fairyproject.bukkit.protocol.packet.artemispacketapi.ArtemisPacketWrapper;
import io.fairyproject.mc.MCPlayer;
import io.fairyproject.mc.protocol.packet.client.CPacketFlying;
import cc.ghast.packet.wrapper.packet.play.client.GPacketPlayClientFlying;

public class CPacketArtemisFlying extends ArtemisPacketWrapper<GPacketPlayClientFlying> implements CPacketFlying {
    public CPacketArtemisFlying(GPacketPlayClientFlying wrapper, MCPlayer channel) {
        super(wrapper, channel);
    }

    @Override
    public boolean isGround() {
        return wrapper.isOnGround();
    }
}
