package me.lofienjoyer.TownyElections.commands.party;

import me.lofienjoyer.TownyElections.TownyElections;
import me.lofienjoyer.TownyElections.commands.SubCommand;
import me.lofienjoyer.TownyElections.parties.Party;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class PartyInfoSubCommand extends SubCommand {

    public PartyInfoSubCommand() {
        super("info", 1);
    }

    @Override
    public boolean execute(Player player, String[] args) {
        Party party;
        switch (args[0].toLowerCase()) {
            case "town": {
                if (!TownyElections.hasPerms(player, TownyElections.Permissions.TOWNPARTY_INFO)) return true;
                party = partyManager.getPlayerTownParty(player.getUniqueId());
            } break;
            case "nation": {
                if (!TownyElections.hasPerms(player, TownyElections.Permissions.NATIONPARTY_INFO)) return true;
                party = partyManager.getPlayerNationParty(player.getUniqueId());
            } break;
            default:
                return false;
        }

        if (party == null) {
            player.sendMessage(TownyElections.getMessage("not-in-a-party"));
            return true;
        }

        StringBuilder builder = new StringBuilder();
        builder.append(ChatColor.GOLD + "" + ChatColor.BOLD + party.getName() + "\n" + ChatColor.RESET);
        builder.append(TownyElections.getMessage("leader"));
        builder.append(Bukkit.getOfflinePlayer(party.getLeader()).getName());
        builder.append("\n");
        builder.append(TownyElections.getMessage("assistants"));
        if (party.getAssistants().size() != 0) {
            builder.append(party.getAssistants().get(0));
            for (int i = 1; i < party.getAssistants().size(); i++) {
                builder.append(", ");
                builder.append(Bukkit.getOfflinePlayer(party.getAssistants().get(0)).getName());
            }
        }
        builder.append("\n");
        builder.append(TownyElections.getMessage("members"));
        builder.append(Bukkit.getOfflinePlayer(party.getMembers().get(0)).getName());
        for (int i = 1; i < party.getMembers().size(); i++) {
            builder.append(", ");
            builder.append(Bukkit.getOfflinePlayer(party.getMembers().get(i)).getName());
        }
        player.sendMessage(builder.toString());
        return true;
    }

}
