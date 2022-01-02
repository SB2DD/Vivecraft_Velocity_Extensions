package me.polishkrowa.vivecraft_velocity_extensions;

import com.google.inject.Inject;
import com.velocitypowered.api.event.connection.PluginMessageEvent;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.messages.ChannelIdentifier;
import com.velocitypowered.api.proxy.messages.LegacyChannelIdentifier;
import com.velocitypowered.api.proxy.messages.MinecraftChannelIdentifier;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import org.slf4j.Logger;


@Plugin(
        id = "vivecraft_velocity_extensions",
        name = "Vivecraft_Velocity_Extensions",
        version = "1.0"
)
public class Vivecraft_Velocity_Extensions {

    private Logger logger;
    private ProxyServer server;

    MinecraftChannelIdentifier main = MinecraftChannelIdentifier.create("vivecraft", "data");
    LegacyChannelIdentifier legacy = new LegacyChannelIdentifier("Vivecraft");

    @Inject
    public Vivecraft_Velocity_Extensions(ProxyServer server, Logger logger) {
        this.logger = logger;
        this.server = server;
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        server.getChannelRegistrar().register(main, legacy);
    }

    @Subscribe
    public void onProxyShutdown(ProxyShutdownEvent event) {
        server.getChannelRegistrar().unregister(main, legacy);
    }

    @Subscribe
    public void onPluginMessage(PluginMessageEvent event) {
        ChannelIdentifier channel = event.getIdentifier();
        if (channel != main && channel != legacy)
            return;
        if (event.getTarget() instanceof Player) {
            Player player = (Player)event.getTarget();
            player.sendPluginMessage(channel, event.getData());
        } else if (event.getTarget() instanceof RegisteredServer) {
            RegisteredServer server = (RegisteredServer)event.getTarget();
            server.sendPluginMessage(channel, event.getData());
        }
    }

}
