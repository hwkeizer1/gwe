package nl.gwe.domain;

import lombok.Data;

@Data
public class YearUsage {

	private Integer year;
	private MeterValues usages;
}
