package net.pladema.snvs.domain.report;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.pladema.snvs.domain.event.SnvsEventInfoBo;
import net.pladema.snvs.domain.institution.InstitutionDataBo;
import net.pladema.snvs.domain.patient.PatientDataBo;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
public class SnvsToReportBo {

	private Integer groupEventId;

	private Integer eventId;

	private Integer manualClassificationId;

	private LocalDate date;

	private InstitutionDataBo institution;

	private Integer sisaRegisteredId;

	private PatientDataBo patient;

	private Integer professionalId;

	public SnvsToReportBo(SnvsEventInfoBo snvsEventInfo, LocalDate date, PatientDataBo patientDataBo, InstitutionDataBo institution, Integer professionalId){
		this.groupEventId = snvsEventInfo.getGroupEventId();
		this.eventId = snvsEventInfo.getEventId();
		this.manualClassificationId = snvsEventInfo.getManualClassificationId();
		this.date = date;
		this.professionalId = professionalId;
		this.patient = patientDataBo;
		this.institution = institution;
	}

	public Integer getPatientId() {
		return patient == null ? null : patient.getId();
	}

	public Integer getInstitutionId() {
		return institution == null ? null : institution.getId();
	}

	public String getInstitutionSisaCode() {
		return institution == null ? null : institution.getSisaCode();
	}

}
