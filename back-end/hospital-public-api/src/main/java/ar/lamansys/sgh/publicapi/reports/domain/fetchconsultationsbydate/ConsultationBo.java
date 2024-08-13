package ar.lamansys.sgh.publicapi.reports.domain.fetchconsultationsbydate;

import java.util.List;
import java.util.Objects;

import ar.lamansys.sgh.publicapi.reports.domain.ClinicalSpecialtyBo;
import ar.lamansys.sgh.publicapi.activities.domain.datetimeutils.DateBo;
import ar.lamansys.sgh.publicapi.activities.domain.datetimeutils.DateTimeBo;
import ar.lamansys.sgh.publicapi.reports.domain.HierarchicalUnitBo;
import ar.lamansys.sgh.publicapi.reports.domain.IdentificationBo;
import ar.lamansys.sgh.publicapi.reports.domain.MedicalCoverageBo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
public class ConsultationBo {
	private Integer appointmentId;
	private Integer consultationId;
	private DateTimeBo consultationDate;
	private String institutionCode;
	private String institutionName;
	@Setter
	private HierarchicalUnitBo serviceHierarchicalUnit;
	private HierarchicalUnitBo hierarchicalUnit;
	private ClinicalSpecialtyBo clinicalSpecialty;
	private IdentificationBo identification;
	private MedicalCoverageBo medicalCoverage;
	private String officialGender;
	private DateBo birthdate;
	private Integer age;
	private String department;
	private String city;
	private String appointmentState;
	private String appointmentBookingChannel;
	@Setter
	private List<ConsultationItemWithDateBo> reasons;
	@Setter
	private List<ConsultationItemWithDateBo> procedures;
	@Setter
	private List<ConsultationItemWithDateBo> problems;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof ConsultationBo)) return false;
		ConsultationBo that = (ConsultationBo) o;
		return Objects.equals(getAppointmentId(), that.getAppointmentId());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getAppointmentId());
	}
}
