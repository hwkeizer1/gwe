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
	
	private float lowElectricityPurchased;
	
	private float lowElectricityDelivered;
	
	private float highElectricityPurchased;
	
	private float highElectricityDelivered;
	
	private float gasPurchased;
	
	private float waterPurchased;

	private MeterValues(Builder builder) {
		this.lowElectricityPurchased = builder.lowElectricityPurchased;
		this.lowElectricityDelivered = builder.lowElectricityDelivered;
		this.highElectricityPurchased = builder.highElectricityPurchased;
		this.highElectricityDelivered = builder.highElectricityDelivered;
		this.gasPurchased = builder.gasPurchased;
		this.waterPurchased = builder.waterPurchased;
	}
	
	public static class Builder {
		
		private float lowElectricityPurchased;
		
		private float lowElectricityDelivered;
		
		private float highElectricityPurchased;
		
		private float highElectricityDelivered;
		
		private float gasPurchased;
		
		private float waterPurchased;

		public Builder setLowElectricityPurchased(float lowElectricityPurchased) {
			this.lowElectricityPurchased = lowElectricityPurchased;
			return this;
		}
		
		public Builder setLowElectricityDelivered(float lowElectricityDelivered) {
			this.lowElectricityDelivered = lowElectricityDelivered;
			return this;
		}
		
		public Builder setHighElectricityPurchased(float highElectricityPurchased) {
			this.highElectricityPurchased = highElectricityPurchased;
			return this;
		}
		
		public Builder setHighElectricityDelivered(float highElectricityDelivered) {
			this.highElectricityDelivered = highElectricityDelivered;
			return this;
		}
		
		public Builder setGasPurchased(float gasPurchased) {
			this.gasPurchased = gasPurchased;
			return this;
		}
		
		public Builder setWaterPurchased(float waterPurchased) {
			this.waterPurchased = waterPurchased;
			return this;
		}
		
		public MeterValues build() {
			return new MeterValues(this);
		}
	}
}
