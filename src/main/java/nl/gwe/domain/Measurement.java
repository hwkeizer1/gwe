package nl.gwe.domain;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import lombok.Data;

@Data
@Entity
public class Measurement implements Comparable<Measurement>{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private LocalDate startDate;
	
	private LocalDate endDate;
	
	@OneToOne(cascade = CascadeType.ALL)
	private MeterValues meterValues;
	
	@OneToOne(cascade = CascadeType.ALL)
	private MeterValues usages;

	@Override
	public String toString() {
		return "id=" + id + ", startDate=" + startDate + ", endDate=" + endDate + ", usages="
				+ usages + "\n";
	}

	@Override
	public int compareTo(Measurement measurement) {
		return startDate.compareTo(measurement.getStartDate());
	}
	
	
	
}
