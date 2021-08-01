package nl.gwe.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class MeterValues {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private int lowElectricityPurchased;
	
	private int lowElectricityDelivered;
	
	private int highElectricityPurchased;
	
	private int highElectricityDelivered;
	
	private int gasPurchased;
	
	private int waterPurchased;

	private MeterValues(Builder builder) {
		this.lowElectricityPurchased = builder.lowElectricityPurchased;
		this.lowElectricityDelivered = builder.lowElectricityDelivered;
		this.highElectricityPurchased = builder.highElectricityPurchased;
		this.highElectricityDelivered = builder.highElectricityDelivered;
		this.gasPurchased = builder.gasPurchased;
		this.waterPurchased = builder.waterPurchased;
	}
	
	public static class Builder {
		
		private int lowElectricityPurchased;
		
		private int lowElectricityDelivered;
		
		private int highElectricityPurchased;
		
		private int highElectricityDelivered;
		
		private int gasPurchased;
		
		private int waterPurchased;

		public Builder setLowElectricityPurchased(int lowElectricityPurchased) {
			this.lowElectricityPurchased = lowElectricityPurchased;
			return this;
		}
		
		public Builder setLowElectricityDelivered(int lowElectricityDelivered) {
			this.lowElectricityDelivered = lowElectricityDelivered;
			return this;
		}
		
		public Builder setHighElectricityPurchased(int highElectricityPurchased) {
			this.highElectricityPurchased = highElectricityPurchased;
			return this;
		}
		
		public Builder setHighElectricityDelivered(int highElectricityDelivered) {
			this.highElectricityDelivered = highElectricityDelivered;
			return this;
		}
		
		public Builder setGasPurchased(int gasPurchased) {
			this.gasPurchased = gasPurchased;
			return this;
		}
		
		public Builder setWaterPurchased(int waterPurchased) {
			this.waterPurchased = waterPurchased;
			return this;
		}
		
		public MeterValues build() {
			return new MeterValues(this);
		}
	}
}
