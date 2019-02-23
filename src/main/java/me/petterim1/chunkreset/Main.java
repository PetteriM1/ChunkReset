package me.petterim1.chunkreset;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerMoveEvent;
import cn.nukkit.event.player.PlayerQuitEvent;
import cn.nukkit.level.format.generic.BaseFullChunk;
import cn.nukkit.plugin.PluginBase;

import java.util.ArrayList;
import java.util.List;

public class Main extends PluginBase implements Listener {

    List<BaseFullChunk> chunks = new ArrayList<>();
    List<String> players = new ArrayList<>();

    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
    }

    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (cmd.getName().equalsIgnoreCase("resetchunk")) {
            if (!sender.hasPermission("chunk.reset")) {
                sender.sendMessage("\u00A73[ChunkReset] \u00A7cYou don't have permission to use this command");
                return true;
            }

            if (!(sender instanceof Player)) {
                sender.sendMessage("\u00A73[ChunkReset] \u00A7cYou can run this command only as player");
                return true;
            }

            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("toggle")) {
                    if (players.contains(sender.getName())) {
                        players.remove(sender.getName());
                        chunks.clear();
                        sender.sendMessage("\u00A73[ChunkReset] \u00A7aReseting chunks stopped");
                    } else {
                        players.add(sender.getName());
                        chunks.clear();
                        sender.sendMessage("\u00A73[ChunkReset] \u00A7aNow reseting chunks you walk on");
                    }
                    return true;
                }
            }

            Player p = (Player) sender;

            try {
                p.getLevel().regenerateChunk(p.getChunkX(), p.getChunkZ());
            } catch (Exception e) {
                sender.sendMessage("\u00A73[ChunkReset] \u00A7aFailed!");
                return true;
            }

            sender.sendMessage("\u00A73[ChunkReset] \u00A7aDone!");

            return true;
        }
        return false;
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        players.remove(e.getPlayer().getName());
        chunks.clear();
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        if (e.getFrom().equals(e.getTo())) return;
        Player p = e.getPlayer();
        if (players.contains(p.getName())) {
            if (!chunks.contains(p.getLevel().getChunk(p.getChunkX(), p.getChunkZ()))) {
                p.getLevel().regenerateChunk(p.getChunkX(), p.getChunkZ());
            }
        }
    }
}
