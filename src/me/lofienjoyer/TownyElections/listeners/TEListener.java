package me.lofienjoyer.TownyElections.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import me.lofienjoyer.TownyElections.TownyElections;
import me.lofienjoyer.TownyElections.elections.NationElection;
import me.lofienjoyer.TownyElections.elections.TownElection;
import me.lofienjoyer.TownyElections.parties.NationParty;
import me.lofienjoyer.TownyElections.parties.TownParty;
import com.palmergames.bukkit.towny.event.TownRemoveResidentEvent;

public class TEListener implements Listener {
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		if (e.getPlayer().hasPermission("townyelections.vote.town")) {
			TownElection election;
			election = TownyElections.getInstance().getElectionManager().getTownElection(e.getPlayer());
			if (election == null) return;
			e.getPlayer().sendTitle(" ", TownyElections.getMessage("active-election"), 20, 60, 20);
		}
	}
	
	@EventHandler
	public void onPlayerTownLeave(TownRemoveResidentEvent e) {
		Player player = Bukkit.getPlayer(e.getResident().getName());
		
		TownParty townParty = TownyElections.getInstance().getPartyManager().getPlayerTownParty(player.getUniqueId());
		if (townParty != null) {
			townParty.removeMember(player.getUniqueId());
		}
		
		NationParty nationParty = TownyElections.getInstance().getPartyManager().getPlayerNationParty(player.getUniqueId());
		if (nationParty != null) {
			nationParty.removeMember(player.getUniqueId());
		}
		
		TownElection townElection = TownyElections.getInstance().getElectionManager().getTownElection(player);
		if (townElection != null) {
			townElection.removeVote(player.getUniqueId());
		}
		
		NationElection nationElection = TownyElections.getInstance().getElectionManager().getNationElection(player);
		if (nationElection != null) {
			nationElection.removeVote(player.getUniqueId());
		}
	}

}
