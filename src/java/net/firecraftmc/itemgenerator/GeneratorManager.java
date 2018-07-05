package net.firecraftmc.itemgenerator;

import net.firecraftmc.shared.classes.FirecraftMC;
import net.firecraftmc.shared.classes.Messages;
import net.firecraftmc.shared.classes.Prefixes;
import net.firecraftmc.shared.classes.Utils;
import net.firecraftmc.shared.classes.enums.Rank;
import net.firecraftmc.shared.classes.model.player.FirecraftPlayer;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class GeneratorManager implements CommandExecutor {

    private ItemGenerator plugin;
    private Map<Location, Generator> gens = new HashMap<>();

    public GeneratorManager(ItemGenerator plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String l, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cOnly players may use that command.");
            return true;
        }

        FirecraftPlayer player = FirecraftMC.getPlayer(((Player) sender).getUniqueId());

        if (!player.getMainRank().isEqualToOrHigher(Rank.ADMIN)) {
            player.sendMessage(Messages.noPermission);
            return true;
        }

        if (!(args.length > 0)) {
            player.sendMessage(Prefixes.GENERATORS + "<ec>Usage: /generator <set|remove> [material] [time]");
            return true;
        }

        if (Utils.Command.checkCmdAliases(args, 0, "set", "s")) {
            Block block = player.getPlayer().getTargetBlock(null, 100);
            if (block == null) {
                player.sendMessage(Prefixes.GENERATORS + "<ec>You are not looking at a valid block.");
                return true;
            }

            if (args.length != 3) {
                player.sendMessage(Prefixes.GENERATORS + "<ec>Usage: /generator set [material] [time]");
                return true;
            }

            Material material;
            double time;

            try {
                material = Material.valueOf(args[1].toUpperCase());
            } catch (Exception e) {
                player.sendMessage(Prefixes.GENERATORS + "<ec>That is not a valid material for an item.");
                return true;
            }

            try {
                time = Double.parseDouble(args[2]);
            } catch (Exception e) {
                player.sendMessage(Prefixes.GENERATORS + "<ec>That is not a valid time.");
                return true;
            } 

            Generator generator = new Generator(block, new ItemStack(material), time);
            this.gens.put(block.getLocation(), generator);
            generator.runTaskTimer(plugin, 0, 1);
            player.sendMessage(Prefixes.GENERATORS + "<nc>You created a generator for the item <vc>" + args[1] + " <nc>with an interval of <vc>" + time);
        } else if (Utils.Command.checkCmdAliases(args, 0, "remove", "r")) {
            Block block = player.getPlayer().getTargetBlock(null, 100);
            if (block == null) {
                player.sendMessage(Prefixes.GENERATORS + "<ec>You are not looking at a valid block.");
                return true;
            }

            if (!this.gens.containsKey(block.getLocation())) {
                player.sendMessage(Prefixes.GENERATORS + "<ec>That is not a valid generator.");
                return true;
            }

            this.gens.get(block.getLocation()).cancel();
            this.gens.remove(block.getLocation());
            player.sendMessage(Prefixes.GENERATORS + "<nc>You removed that generator.");
        } else {
            player.sendMessage(Prefixes.GENERATORS + "<ec>That is not a valid sub command.");
            return true;
        }

        return true;
    }
}