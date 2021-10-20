package nl.gwe.converters;

import java.text.DateFormatSymbols;

import javafx.util.StringConverter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class IntegerToMonthConverter extends StringConverter<Number> {

	@Override
	public String toString(Number month) {
		int monthNumber = month.intValue();
		if (monthNumber == 0 || monthNumber == 13) return "";
		return new DateFormatSymbols().getMonths()[monthNumber-1];
	}

	@Override
	public Integer fromString(String string) {
		// no need for this conversion
		return null;
	}



}
