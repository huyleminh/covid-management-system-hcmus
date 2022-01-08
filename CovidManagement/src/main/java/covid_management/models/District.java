package covid_management.models;

public class District {
	private int districtId;
	private String districtName;
	private int provinceId;

	public District(int districtId, String districtName, int provinceId) {
		this.districtId = districtId;
		this.districtName = districtName;
		this.provinceId = provinceId;
	}

	public int getDistrictId() {
		return districtId;
	}

	public String getDistrictName() {
		return districtName;
	}

	public int getProvinceId() {
		return provinceId;
	}
}
