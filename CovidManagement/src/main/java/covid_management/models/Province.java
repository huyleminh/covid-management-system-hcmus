package covid_management.models;

public class Province {
	private int provinceId;
	private String provinceName;

	public Province(int provinceId, String provinceName) {
		this.provinceId = provinceId;
		this.provinceName = provinceName;
	}

	public int getProvinceId() {
		return provinceId;
	}

	public String getProvinceName() {
		return provinceName;
	}
}
