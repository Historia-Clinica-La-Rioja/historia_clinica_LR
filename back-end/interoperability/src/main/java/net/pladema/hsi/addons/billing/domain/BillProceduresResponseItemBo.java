package net.pladema.hsi.addons.billing.domain;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.Value;

@Getter
@ToString
@EqualsAndHashCode
public class BillProceduresResponseItemBo {
	String code;
	String description;
	String snomedPt;
	Integer amount;
	LocalDateTime date;
	Float rate;
	Float coveragePercentage;
	Float coverageRate;
	Float patientRate;
	Float total;

	public BillProceduresResponseItemBo(String code, String description, String snomedPt, Integer amount, LocalDateTime date, Float rate, Float coveragePercentage, Float coverageRate, Float patientRate, Float total) {
		this.code = code == null ? "" : code;
		this.description = description == null ? "" : description;
		this.snomedPt = snomedPt == null ? "" : snomedPt;
		this.amount = amount == null ? 0 : amount;
		this.date = date;
		this.rate = zeroIfNull(rate);
		this.coveragePercentage = zeroIfNull(coveragePercentage);
		this.coverageRate = zeroIfNull(coverageRate);
		this.patientRate = zeroIfNull(patientRate);
		this.total = zeroIfNull(total);
	}

	private static Float zeroIfNull(Float value) {
		return value == null ? 0.0F : value;
	}
}
