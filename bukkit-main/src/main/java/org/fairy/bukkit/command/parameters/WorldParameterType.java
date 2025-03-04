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

package org.fairy.bukkit.command.parameters;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.fairy.bean.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
public class WorldParameterType extends BukkitParameterHolder<World> {

	@Override
	public World transform(final CommandSender sender, final String source) {
		final World world = Bukkit.getServer().getWorld(source);

		if (world == null) {
			sender.sendMessage(ChatColor.RED + "No world with the name " + source + " found.");
			return (null);
		}

		return (world);
	}

	@Override
	public List<String> tabComplete(final Player sender, final Set<String> flags, final String source) {
		final List<String> completions = new ArrayList<>();

		for (final World world : Bukkit.getServer().getWorlds()) {
			if (StringUtils.startsWithIgnoreCase(world.getName(), source)) {
				completions.add(world.getName());
			}
		}

		return (completions);
	}

	@Override
	public Class[] type() {
		return new Class[] {World.class};
	}
}