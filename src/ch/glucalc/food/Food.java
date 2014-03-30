package ch.glucalc.food;

public class Food {

	private long id;
	private String name;
	private float quantity;
	private float carbonhydrate;
	private String unit;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public float getQuantity() {
		return quantity;
	}

	public void setQuantity(float quantity) {
		this.quantity = quantity;
	}

	public float getCarbonhydrate() {
		return carbonhydrate;
	}

	public void setCarbonhydrate(float carbonhydrate) {
		this.carbonhydrate = carbonhydrate;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

}
