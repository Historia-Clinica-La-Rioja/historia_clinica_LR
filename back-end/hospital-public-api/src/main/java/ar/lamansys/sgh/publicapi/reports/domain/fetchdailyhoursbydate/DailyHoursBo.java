package ar.lamansys.sgh.publicapi.reports.domain.fetchdailyhoursbydate;

import java.util.List;

import ar.lamansys.sgh.publicapi.domain.ClinicalSpecialtyBo;
import ar.lamansys.sgh.publicapi.reports.domain.HierarchicalUnitBo;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class DailyHoursBo {
	private Integer diaryId;
	private String institutionCode;
	private String institutionName;
	private HierarchicalUnitBo serviceHierarchicalUnit;
	private HierarchicalUnitBo hierarchicalUnit;
	private String professionalCuil;
	private ProfessionalDataBo professionalData;
	private ClinicalSpecialtyBo clinicalSpecialty;
	private String diaryType;
	private Long minutesInAppointments;
	private Long possibleAppointments;
	private Long interruptions;
	private List<String> interruptionsDescriptions;
}
