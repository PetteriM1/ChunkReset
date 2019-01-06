package me.petterim1.chunkreset;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.plugin.PluginBase;

public class Main extends PluginBase {

    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (cmd.getName().equalsIgnoreCase("resetchunk")) {
            if (!sender.hasPermission("chunk.reset")) {
                sender.sendMessage("\u00A7cYou don't have permission to use this command");
                return true;
            }

            if (!(sender instanceof Player)) {
                sender.sendMessage("\u00A7cYou can run this command only as player");
                return true;
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
}
