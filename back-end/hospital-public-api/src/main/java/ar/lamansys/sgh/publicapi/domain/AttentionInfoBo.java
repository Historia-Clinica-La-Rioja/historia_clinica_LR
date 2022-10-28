package ar.lamansys.sgh.publicapi.domain;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
public class AttentionInfoBo {

	private Long id;
	private Long encounterId;
	private LocalDate attentionDate;
	private SnomedBo speciality;
	private PersonInfoBo patient;
	private CoverageActivityInfoBo coverage;
	private ScopeEnum scope;
	private InternmentBo internmentInfo;
	private ProfessionalBo responsibleDoctor;
	private SingleDiagnosticBo singleDiagnosticBo;

}
