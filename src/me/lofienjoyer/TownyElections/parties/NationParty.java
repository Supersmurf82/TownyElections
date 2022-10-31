package me.lofienjoyer.TownyElections.parties;

import java.util.ArrayList;
import java.util.UUID;

import com.palmergames.bukkit.towny.TownyUniverse;
import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import com.palmergames.bukkit.towny.object.Nation;

public class NationParty extends Party {
	
	private Nation nation;

	public NationParty(String name, UUID leader, UUID territory) {
		super(name, leader, PartyType.NATION, territory);
		
		setup();
	}
	
	@Override
	public void setup() {
		if (members == null) members = new ArrayList<UUID>();
		if (assistants == null) assistants = new ArrayList<UUID>();
		if (invites == null) invites = new ArrayList<UUID>();
		
		try {
			nation = TownyUniverse.getInstance().getDataSource().getNation(territory);
		} catch (NotRegisteredException e) {}
	}
	
	public Nation getNation() {
		return nation;
	}

}
