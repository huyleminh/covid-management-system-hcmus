package com.models;

public class Location {
	private int locationId;
	private String locationName;
	private short capacity;
	private short availableSlots;

	public Location(int locationId, String locationName, short capacity, short availableSlots) {
		this.locationId = locationId;
		this.locationName = locationName;
		this.capacity = capacity;
		this.availableSlots = availableSlots;
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

	public short getAvailableSlots() {
		return availableSlots;
	}

	// Testing
	public void logToScreen() {
		System.out.println(">>> Location <<<");
		System.out.println("locationId 	   = " + locationId);
		System.out.println("locationName   = " + locationName);
		System.out.println("capacity 	   = " + capacity);
		System.out.println("availableSlots = " + availableSlots);
	}
}
