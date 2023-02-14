package ar.lamansys.sgh.shared.infrastructure.input.service.odontology;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class OdontologyDiagnosticProcedureInfoDto {

	private Integer id;
	private Integer patientId;
	private Integer snomedId;
	private Integer toothId;
	private Integer surfaceId;
	private String cie10Codes;
	private LocalDate performedDate;
	private Long noteId;
	private boolean isDiagnostic;

}
