package ar.lamansys.sgh.publicapi.activities.domain;

import java.time.LocalDate;

import ar.lamansys.sgh.publicapi.activities.domain.datetimeutils.DateTimeBo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
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
	private DateTimeBo attentionDateWithTime;
	private PersonInfoExtendedBo personInfoExtended;
	private DateTimeBo emergencyCareAdministrativeDischargeDateTime;
	private Boolean billable;

}
