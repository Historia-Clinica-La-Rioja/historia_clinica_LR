package net.pladema.hsi.addons.billing.infrastructure.input.domain;

import java.util.List;
import java.util.stream.Collectors;

import lombok.Value;
import net.pladema.hsi.addons.billing.domain.BillProceduresResponseBo;

@Value
public class BillProceduresResponseDto {
	List<BillProceduresResponseItemDto> procedures;
	private Float medicalCoverageTotal;
	private Float patientTotal;

	private String medicalCoverageName;
	private String medicalCoverageCuit;

	public static BillProceduresResponseDto fromBo(BillProceduresResponseBo response) {
		return new BillProceduresResponseDto(
				response.getProcedures().stream().map(BillProceduresResponseItemDto::fromBo).collect(Collectors.toList()),
				response.getMedicalCoverageTotal(),
				response.getPatientTotal(),
				response.getMedicalCoverageName(),
				response.getMedicalCoverageCuit()
		);
	}
}
