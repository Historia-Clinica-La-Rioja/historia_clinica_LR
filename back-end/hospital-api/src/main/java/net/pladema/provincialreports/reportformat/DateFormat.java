package net.pladema.provincialreports.reportformat;
import lombok.SneakyThrows;

import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class DateFormat {
	@SneakyThrows
	public String reformatDate (String previousDate) {

		SimpleDateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy");
		Date date = inputFormat.parse(previousDate);
		SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy");
		return outputFormat.format(date);
	}

	public String reformatDateTwo(String previousDate) {
		SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date= null;
		try {
			date = inputFormat.parse(previousDate);
		} catch (ParseException e) {
			throw new RuntimeException(e);

		}

		SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
		return outputFormat.format(date);

	}

	@SneakyThrows

	public String ReformatDateThree(String previousDate) {
		SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date date= null;
		try {
			date = inputFormat.parse(previousDate);
		} catch (ParseException e) {
			throw new RuntimeException(e);

		}
		SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy");
		return outputFormat.format(date);
	}

	public String ReformatDateFour (String previousDate) {
		SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.ss");
		Date date= null;
		try {
			date = inputFormat.parse(previousDate);
		} catch (ParseException e) {
			throw new RuntimeException(e);

		}

		SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
		return outputFormat.format(date);

	}


	@SneakyThrows
	public String ReformatDateFive (String previousDate) {

		if (previousDate !=null) {
			SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
			Date date = inputFormat.parse(previousDate);

			SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy");
			return outputFormat.format(date);
		} else {
			return "NO ESPECIFICADO";
		}
	}


}
