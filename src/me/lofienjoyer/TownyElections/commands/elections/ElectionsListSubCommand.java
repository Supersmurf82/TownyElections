package me.lofienjoyer.TownyElections.commands.elections;

import me.lofienjoyer.TownyElections.TownyElections;
import me.lofienjoyer.TownyElections.commands.SubCommand;
import me.lofienjoyer.TownyElections.elections.Election;
import me.lofienjoyer.TownyElections.elections.NationElection;
import me.lofienjoyer.TownyElections.elections.TownElection;
import me.lofienjoyer.TownyElections.parties.Party;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ElectionsListSubCommand extends SubCommand {

    public ElectionsListSubCommand() {
        super("list", 1);
    }

    @Override
    public boolean execute(Player player, String[] args) {
        Election election;
        List<Party> parties = new ArrayList<>();

        switch (args[0].toLowerCase()) {

            case "town": {
                if (!TownyElections.hasPerms(player, TownyElections.Permissions.TOWN_LIST)) return true;

                election = electionManager.getTownElection(player);
                if (election == null) {
                    player.sendMessage(TownyElections.getMessage("not-active-election"));
                    return true;
                }
                parties.addAll(partyManager.getPartiesForTown(((TownElection) election).getTown().getName()));
            } break;

            case "nation": {
                if (!TownyElections.hasPerms(player, TownyElections.Permissions.NATION_LIST)) return true;

                election = electionManager.getNationElection(player);
                if (election == null) {
                    player.sendMessage(TownyElections.getMessage("not-active-election"));
                    return true;
                }
                parties.addAll(partyManager.getPartiesForTown(((NationElection) election).getNation().getName()));
            } break;

            default:
                return false;

        }

        StringBuilder builder = new StringBuilder();
        builder.append(ChatColor.GOLD + "Candidates:");
        if (parties.size() <= 0) {
            builder.append(ChatColor.RED + "There are no candidates");
            player.sendMessage(builder.toString());
            return true;
        }
        for (Party party : parties) {
            builder.append("\n-");
            builder.append(party.getName());
        }
        player.sendMessage(builder.toString());
        return true;
    }

}
