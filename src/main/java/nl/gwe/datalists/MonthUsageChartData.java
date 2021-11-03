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
public class MonthUsageChartData {
	
	private final MonthUsageList monthUsageList;
	
	public MonthUsageChartData(MonthUsageList monthUsageList) {
		this.monthUsageList = monthUsageList;
	}

	public EnumMap<Meters, List<ChartData>> getChartDataForYear(Integer year) {
		Map<Integer, MonthUsage> monthUsages = getMonthUsagesDataForYear(year);
		EnumMap<Meters, List<ChartData>> meterData = new EnumMap<>(Meters.class);
		
		List<ChartData> lowElectricityPurchased = new ArrayList<>();
		List<ChartData> lowElectricityDelivered = new ArrayList<>();
		List<ChartData> highElectricityPurchased = new ArrayList<>();
		List<ChartData> highElectricityDelivered = new ArrayList<>();
		List<ChartData> totalElectricity = new ArrayList<>();
		List<ChartData> gasPurchased = new ArrayList<>();
		List<ChartData> waterPurchased = new ArrayList<>();
		
		for (Map.Entry<Integer, MonthUsage> entry : monthUsages.entrySet()) {
			lowElectricityPurchased.add(new ChartData(entry.getKey(), entry.getValue().getUsages().getLowElectricityPurchased()));
			lowElectricityDelivered.add(new ChartData(entry.getKey(), entry.getValue().getUsages().getLowElectricityDelivered()));
			highElectricityPurchased.add(new ChartData(entry.getKey(), entry.getValue().getUsages().getHighElectricityPurchased()));
			highElectricityDelivered.add(new ChartData(entry.getKey(), entry.getValue().getUsages().getHighElectricityDelivered()));
			totalElectricity.add(new ChartData(entry.getKey(),
					entry.getValue().getUsages().getLowElectricityPurchased() - 
					entry.getValue().getUsages().getLowElectricityDelivered() +
					entry.getValue().getUsages().getHighElectricityPurchased() -
					entry.getValue().getUsages().getHighElectricityDelivered()));
			gasPurchased.add(new ChartData(entry.getKey(), entry.getValue().getUsages().getGasPurchased()));
			waterPurchased.add(new ChartData(entry.getKey(), entry.getValue().getUsages().getWaterPurchased()));
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
	 * Map each monthUsage to the correct month index
	 * Month index 0 is used for the last monthUsage of the previous month if available
	 * Month index 13 is used for the first monthUsage of the next month if available
	 * @param year
	 * @return Map<Integer, MonthUsage>
	 */
	private Map<Integer, MonthUsage> getMonthUsagesDataForYear(Integer year) {
		Map<Integer, MonthUsage> monthUsages = new TreeMap<>();
		if (monthUsageList.getLastMonthUsageOfYear(year - 1).isPresent()) {
			monthUsages.put(0, monthUsageList.getLastMonthUsageOfYear(year - 1).get());
		}
		
		for (MonthUsage monthUsage : monthUsageList.getAllMonthUsageOfYear(year)) {
			monthUsages.put(monthUsage.getDate().getMonthValue(), monthUsage);
		}
		
		if (monthUsageList.getFirstMonthUsageOfYear(year + 1).isPresent()) {
			monthUsages.put(13, monthUsageList.getFirstMonthUsageOfYear(year + 1).get());
		}
		
		return monthUsages;
	}
}
