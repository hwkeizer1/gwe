package nl.gwe.domain;

import java.time.YearMonth;

import javax.persistence.CascadeType;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import lombok.Data;
import nl.gwe.converters.YearMonthDateAttributeConverter;

@Data
@Entity
public class MonthUsage {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id; 
	
	@Convert(converter = YearMonthDateAttributeConverter.class)
	private YearMonth date;
	
	@OneToOne(cascade = CascadeType.ALL)
	private MeterValues meterValues;

	public String getYearMonthLabel() {
		return date.toString();
	}
}
