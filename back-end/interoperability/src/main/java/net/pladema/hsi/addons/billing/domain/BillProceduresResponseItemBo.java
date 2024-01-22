package net.pladema.hsi.addons.billing.domain;

import java.time.LocalDateTime;

import lombok.Value;

@Value
public class BillProceduresResponseItemBo {
	String code;
	String description;
	Integer amount;
	LocalDateTime date;
	Float rate;
	Float coveragePercentage;
	Float coverageRate;
	Float patientRate;
	Float total;
}
