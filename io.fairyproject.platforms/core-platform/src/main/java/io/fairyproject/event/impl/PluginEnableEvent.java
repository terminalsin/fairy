package io.fairyproject.event.impl;

import io.fairyproject.event.Event;
import io.fairyproject.plugin.Plugin;

@Event
public class PluginEnableEvent {
    private final Plugin plugin;

    public PluginEnableEvent(Plugin plugin) {
        this.plugin = plugin;
    }

    public Plugin getPlugin() {
        return plugin;
    }
}
