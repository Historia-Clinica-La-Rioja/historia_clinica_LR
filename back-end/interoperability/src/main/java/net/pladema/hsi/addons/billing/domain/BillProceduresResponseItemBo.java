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
	public enum PracticeType {PRACTICE, NON_REGISTERED, BED, MEDICATION, MODULE;};

	String codeNomenclator;
	String descriptionNomenclator;
	String description;
	Integer amount;
	LocalDateTime date;
	Float rate;
	Float coveragePercentage;
	Float coverageRate;
	Float patientRate;
	Float total;
	PracticeType practiceType;
	public BillProceduresResponseItemBo(String codeNomenclator, String descriptionNomenclator, String description, Integer amount, LocalDateTime date,
		Float rate, Float coveragePercentage, Float coverageRate, Float patientRate, Float total, PracticeType practiceType) {
		this.codeNomenclator = codeNomenclator;
		this.descriptionNomenclator = descriptionNomenclator;
		this.description = description;
		this.amount = amount;
		this.date = date;
		this.rate = rate;
		this.coveragePercentage = coveragePercentage;
		this.coverageRate = coverageRate;
		this.patientRate = patientRate;
		this.total = total;
		this.practiceType = practiceType;
	}
	public boolean isNonRegistered() {
		return this.getPracticeType().equals(PracticeType.NON_REGISTERED);
	}

	public boolean withoutCode() {
		return (this.getCodeNomenclator() == null || this.getCodeNomenclator().isEmpty());
	}

	public boolean withoutDescriptionNomenclator() {
		return (this.getDescriptionNomenclator() == null || this.getDescriptionNomenclator().isEmpty());
	}

	public boolean withoutPrice() {
		return this.rate == null;
	}

	public BillProceduresResponseItemBo useDescriptionAsDescriptionNomenclator() {
		return new BillProceduresResponseItemBo(codeNomenclator, description, descriptionNomenclator, amount, date, rate, coveragePercentage, coverageRate, patientRate, total, practiceType);
	}

	public BillProceduresResponseItemBo clearPrice() {
		return new BillProceduresResponseItemBo(codeNomenclator, descriptionNomenclator, description, null, date, null, null, null, null, null, practiceType);
	}

}
