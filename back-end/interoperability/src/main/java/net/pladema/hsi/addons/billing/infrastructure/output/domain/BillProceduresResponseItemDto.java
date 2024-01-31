package net.pladema.hsi.addons.billing.infrastructure.output.domain;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;
import net.pladema.hsi.addons.billing.domain.BillProceduresResponseItemBo;

@Setter
@Getter
public class BillProceduresResponseItemDto {
	private String description;

	private Integer amount;

	private LocalDateTime date;

	private Float rate;

	private Float coveragePercentage;

	private Float patientRate;

	private Float coverageRate;

	private Float total;

	private String descriptionNomenclator;

	private String codeNomenclator;

	public BillProceduresResponseItemBo toBo() {
		return new BillProceduresResponseItemBo(
		this.getCodeNomenclator(),
		this.getDescriptionNomenclator(),
		this.getDescription(),
		this.getAmount(),
		this.getDate(),
		this.getRate(),
		this.getCoveragePercentage(),
		this.getPatientRate(),
		this.getCoverageRate(),
		this.getTotal()
		);
	}
}
