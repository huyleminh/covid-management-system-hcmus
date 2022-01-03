package com.models;

import com.utilities.UtilityFunctions;

public class Location {
	// An object to check whether connection of the database is unavailable or not.
	public static final Location emptyInstance = new Location(-1, "", (short) -1, (short) -1);

	private int locationId;
	private String locationName;
	private short capacity;
	private short currentSlots;

	public Location(int locationId, String locationName, short capacity, short currentSlots) {
		this.locationId = locationId;
		this.locationName = locationName;
		this.capacity = capacity;
		this.currentSlots = currentSlots;
	}

	public int getLocationId() {
		return locationId;
	}

	public String getLocationName() {
		return locationName;
	}

	public short getCapacity() {
		return capacity;
	}

	public short getCurrentSlots() {
		return currentSlots;
	}

	public boolean isEmpty() {
		return equals(Location.emptyInstance);
	}

	public boolean equals(Location location) {
		return locationId == location.locationId &&
				UtilityFunctions.compareTwoStrings(locationName, location.locationName) &&
				capacity == location.capacity &&
				currentSlots == location.currentSlots;
	}

	// Testing
	public void logToScreen() {
		System.out.println(">>> Location <<<");
		System.out.println("locationId 	 = " + locationId);
		System.out.println("locationName = " + locationName);
		System.out.println("capacity 	 = " + capacity);
		System.out.println("currentSlots = " + currentSlots);
	}
}
