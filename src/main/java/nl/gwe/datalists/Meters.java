package nl.gwe.datalists;

public enum Meters {
	LOW_ELECTRICITY_PURCHASED("Elektra laag afgenomen", "kWh"),
	LOW_ELECTRICITY_DELIVERED("Elektra laag teruggeleverd", "kWh"),
	HIGH_ELECTRICITY_PURCHASED("Elektra normaal afgenomen", "kWh"),
	HIGH_ELECTRICITY_DELIVERED("Elektra normaal teruggeleverd", "kWh"),
	TOTAL_ELECTRICITY("Elektra totaal", "kWh"),
	GAS_PURCHASED("Gas", "m³"),
	WATER_PURCHASED("Water", "m³");
	
	private final String nlName;
	private final String unit;
	
	private Meters(String nlName, String unit) {
		this.nlName = nlName;
		this.unit = unit;
	}
	
	public String nlName() {
		return nlName;
	}
	
	public String unit() {
		return unit;
	}
}
