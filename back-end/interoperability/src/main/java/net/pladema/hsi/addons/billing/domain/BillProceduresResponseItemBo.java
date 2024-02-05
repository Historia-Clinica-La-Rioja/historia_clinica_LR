package net.pladema.hsi.addons.billing.domain;

import java.time.LocalDateTime;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import org.apache.commons.lang3.ObjectUtils;

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
		this.code = ObjectUtils.defaultIfNull(code, "");
		this.description = ObjectUtils.firstNonNull(description, snomedPt, "");
		this.snomedPt = ObjectUtils.defaultIfNull(snomedPt, "");
		this.amount = ObjectUtils.defaultIfNull(amount, 0);
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
