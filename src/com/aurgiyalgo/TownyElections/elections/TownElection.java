package com.aurgiyalgo.TownyElections.elections;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;

import com.aurgiyalgo.TownyElections.TownyElections;
import com.aurgiyalgo.TownyElections.TownyElections.MutableInteger;
import com.aurgiyalgo.TownyElections.parties.TownParty;
import com.palmergames.bukkit.towny.TownyUniverse;
import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import com.palmergames.bukkit.towny.exceptions.TownyException;
import com.palmergames.bukkit.towny.object.Town;

public class TownElection extends Election {

	private Town town;
	
	public TownElection(long endTime, Town town) {
		super(endTime);
		territoryUuid = town.getUuid();
		setup();
	}
	
	public void setup() {
		try {
			town = TownyUniverse.getInstance().getDataSource().getTown(territoryUuid);
		} catch (NotRegisteredException e) {
			e.printStackTrace();
		}
	}
	
	public void addVote(UUID player, String candidate) {
		if (votes.containsKey(player)) return;
		if (TownyElections.getInstance().getPartyManager().getPartiesForTown(town.getName()).stream().filter(party -> party.getName().equals(candidate)).collect(Collectors.toList()).isEmpty()) return;
		votes.put(player, candidate);
	}
	
	public void removeVote(UUID player) {
		if (votes.containsKey(player)) {
			votes.remove(player);
		}
	}
	
	public String finishElection() {
		if (votes.isEmpty()) {
			TownyElections.sendTownMessage(town, TownyElections.getTranslatedMessage("no-winner"));
			return null;
		}
		
		Map<String, MutableInteger> voteCount = new HashMap<String, MutableInteger>();
		
		for (Map.Entry<UUID, String> entry : votes.entrySet())  {
			if (!voteCount.containsKey(entry.getValue())) {
				voteCount.put(entry.getValue(), new MutableInteger(1));
				continue;
			}
			voteCount.get(entry.getValue()).value++;
		}
		
		Map.Entry<String, MutableInteger> maxCandidate = null;
		
		for (Map.Entry<String, MutableInteger> entry : voteCount.entrySet()) {
			if (maxCandidate == null) {
				maxCandidate = entry;
				continue;
			}
			if (maxCandidate.getValue().value == entry.getValue().value) {
				TownyElections.sendTownMessage(town, TownyElections.getTranslatedMessage("no-winner"));
				return null;
			}
			if (maxCandidate.getValue().value < entry.getValue().value) {
				maxCandidate = entry;
				continue;
			}
		}
		
		winner = maxCandidate.getKey();
		
		try {
			TownParty party = TownyElections.getInstance().getPartyManager().getPartiesForTown(town.getName()).stream().filter(pty -> pty.getName().toLowerCase().equals(winner.toLowerCase())).collect(Collectors.toList()).get(0);
			town.setMayor(TownyUniverse.getInstance().getDataSource().getResident(Bukkit.getOfflinePlayer(party.getLeader()).getName()));
			TownyUniverse.getInstance().getDataSource().saveTown(town);
			String msg = TownyElections.getTranslatedMessage("election-won").replace("%party%", party.getName());
			TownyElections.sendTownSubtitle(town, msg);
			
		} catch (NotRegisteredException e) {
			e.printStackTrace();
		} catch (TownyException e) {
			e.printStackTrace();
		}
		return winner;
	}

	public Town getTown() {
		return town;
	}

}
