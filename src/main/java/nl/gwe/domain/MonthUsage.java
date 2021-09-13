package nl.gwe.domain;

import java.time.LocalDate;
import java.time.Month;
import java.util.Comparator;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import lombok.Data;

@Data
@Entity
public class MonthUsage {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id; 
	
	private LocalDate date;
	
	@OneToOne(cascade = CascadeType.ALL)
	private MeterValues meterValues;
	
	public Month getMonth() {
		return this.date.getMonth();
	}
	
	public int getYear() {
		return this.date.getYear();
	}

}
