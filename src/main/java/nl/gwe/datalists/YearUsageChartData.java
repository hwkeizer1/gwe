package nl.gwe.datalists;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import nl.gwe.domain.MeterValues;
import nl.gwe.domain.MonthUsage;

@Slf4j
@Component
public class YearUsageChartData {

private final MonthUsageList monthUsageList;
	
	public YearUsageChartData(MonthUsageList monthUsageList) {
		this.monthUsageList = monthUsageList;
	}

	public EnumMap<Meters, List<YearChartData>> getYearUsageChartData() {
		Map<Integer, MeterValues> yearUsages = getYearUsagesData();
		EnumMap<Meters, List<YearChartData>> meterData = new EnumMap<>(Meters.class);
		
		List<YearChartData> lowElectricityPurchased = new ArrayList<>();
		List<YearChartData> lowElectricityDelivered = new ArrayList<>();
		List<YearChartData> highElectricityPurchased = new ArrayList<>();
		List<YearChartData> highElectricityDelivered = new ArrayList<>();
		List<YearChartData> totalElectricity = new ArrayList<>();
		List<YearChartData> gasPurchased = new ArrayList<>();
		List<YearChartData> waterPurchased = new ArrayList<>();
		
		for (Map.Entry<Integer, MeterValues> entry : yearUsages.entrySet()) {
			lowElectricityPurchased.add(new YearChartData(entry.getKey(), entry.getValue().getLowElectricityPurchased()));
			lowElectricityDelivered.add(new YearChartData(entry.getKey(), entry.getValue().getLowElectricityDelivered()));
			highElectricityPurchased.add(new YearChartData(entry.getKey(), entry.getValue().getHighElectricityPurchased()));
			highElectricityDelivered.add(new YearChartData(entry.getKey(), entry.getValue().getHighElectricityDelivered()));
			totalElectricity.add(new YearChartData(entry.getKey(),
					entry.getValue().getLowElectricityPurchased() - 
					entry.getValue().getLowElectricityDelivered() +
					entry.getValue().getHighElectricityPurchased() -
					entry.getValue().getHighElectricityDelivered()));
			gasPurchased.add(new YearChartData(entry.getKey(), entry.getValue().getGasPurchased()));
			waterPurchased.add(new YearChartData(entry.getKey(), entry.getValue().getWaterPurchased()));
		}
		
		meterData.put(Meters.LOW_ELECTRICITY_PURCHASED, lowElectricityPurchased);
		meterData.put(Meters.LOW_ELECTRICITY_DELIVERED, lowElectricityDelivered);
		meterData.put(Meters.HIGH_ELECTRICITY_PURCHASED, highElectricityPurchased);
		meterData.put(Meters.HIGH_ELECTRICITY_DELIVERED, highElectricityDelivered);
		meterData.put(Meters.TOTAL_ELECTRICITY, totalElectricity);
		meterData.put(Meters.GAS_PURCHASED, gasPurchased);
		meterData.put(Meters.WATER_PURCHASED, waterPurchased);
		
		return meterData;
	}
	
	/**
	 * create a Map with index year and value MeterValues
	 * @return Map<Integer, MeterValues>
	 */
	private Map<Integer, MeterValues> getYearUsagesData() {
		Map<Integer, MeterValues> yearUsages = new TreeMap<>();

		for (Integer year : monthUsageList.getReadOnlyYearList()) {
			List<MonthUsage> monthUsages = monthUsageList.getAllMonthUsageOfYear(year);
			if (monthUsages.size() == 12) {
				MeterValues yearUsage = new MeterValues();
				for (MonthUsage monthUsage : monthUsages) {
					yearUsage.addMeterValues(monthUsage.getUsages());
				}
				yearUsages.put(year, yearUsage);
			}
		}
		return yearUsages;
	}
}
