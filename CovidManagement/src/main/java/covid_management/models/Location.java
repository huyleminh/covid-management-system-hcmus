package covid_management.models;

import shared.utilities.UtilityFunctions;

public class Location {
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

	public boolean equals(Location location) {
		return locationId == location.locationId &&
				UtilityFunctions.compareTwoStrings(locationName, location.locationName) &&
				capacity == location.capacity &&
				currentSlots == location.currentSlots;
	}
}
