package net.pladema.hsi.addons.billing.infrastructure.input.domain;

import java.time.LocalDateTime;

import lombok.Value;
import net.pladema.hsi.addons.billing.domain.BillProceduresResponseItemBo;

@Value
public class BillProceduresResponseItemDto {
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

	public static BillProceduresResponseItemDto fromBo(BillProceduresResponseItemBo bo) {
		return new BillProceduresResponseItemDto(
			bo.getCodeNomenclator(),
			bo.getDescriptionNomenclator(),
			bo.getDescription(),
			bo.getAmount(),
			bo.getDate(),
			bo.getRate(),
			bo.getCoveragePercentage(),
			bo.getCoverageRate(),
			bo.getPatientRate(),
			bo.getTotal()
		);
	}
}
