package rocklike.test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Java8Date {

	public static void main(String[] args) {
		Java8Date main = new Java8Date();
		main.testDate();

	}

	void testDate(){
		LocalDateTime now = LocalDateTime.now();
		LocalDate localDate = now.toLocalDate();
		LocalTime localTime = now.toLocalTime();
		System.out.printf("%s => %s \n", localDate, localTime);
		String format = localTime.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
		String format2 = localTime.format(DateTimeFormatter.ofPattern("hh:mm:ss"));
		System.out.printf("%s => %s \n", format, format2);

	}
}
