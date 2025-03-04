/*
 * MIT License
 *
 * Copyright (c) 2021 Imanity
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.fairy.bukkit.command.presence;

import org.bukkit.ChatColor;
import org.fairy.bukkit.command.event.BukkitCommandEvent;
import org.fairy.command.PresenceProvider;

public class DefaultPresenceProvider extends PresenceProvider<BukkitCommandEvent> {

    @Override
    public Class<BukkitCommandEvent> type() {
        return BukkitCommandEvent.class;
    }

    @Override
    public void sendUsage(BukkitCommandEvent event, String usage) {
        event.getSender().sendMessage(ChatColor.RED + "Usage: " + usage);
    }

    @Override
    public void sendError(BukkitCommandEvent event, Throwable throwable) {
        event.getSender().sendMessage(ChatColor.RED + "It appears there was some issues processing your command...");
    }

    @Override
    public void sendNoPermission(BukkitCommandEvent event) {
        event.getSender().sendMessage(ChatColor.RED + "No permission.");
    }

    @Override
    public void sendInternalError(BukkitCommandEvent event, String message) {
        event.getSender().sendMessage(ChatColor.RED + message);
    }
}
