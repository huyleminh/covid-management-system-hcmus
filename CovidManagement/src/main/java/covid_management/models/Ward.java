package covid_management.models;

public class Ward {
	private int wardId;
	private String wardName;
	private int districtId;

	public Ward(int wardId, String wardName, int districtId) {
		this.wardId = wardId;
		this.wardName = wardName;
		this.districtId = districtId;
	}

	public int getWardId() {
		return wardId;
	}

	public String getWardName() {
		return wardName;
	}

	public int getDistrictId() {
		return districtId;
	}
}
