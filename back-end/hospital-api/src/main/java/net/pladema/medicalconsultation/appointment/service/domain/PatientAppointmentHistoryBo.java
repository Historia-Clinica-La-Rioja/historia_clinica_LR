package net.pladema.medicalconsultation.appointment.service.domain;

import ar.lamansys.sgh.clinichistory.domain.ips.SnomedBo;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
public class PatientAppointmentHistoryBo {

	private Integer diaryId;

	private LocalDate date;

	private LocalTime time;

	private String institution;

	private String city;

	private Integer doctorPersonId;

	private String doctorName;

	private String clinicalSpecialty;

	private List<SnomedBo> practices;

	private String service;

	private Short statusId;

	public PatientAppointmentHistoryBo(Integer diaryId, LocalDate date, LocalTime time, String institution, String city, Integer doctorPersonId, String clinicalSpecialty,
									   String service, Short statusId) {
		this.diaryId = diaryId;
		this.date = date;
		this.time = time;
		this.institution = institution;
		this.city = city;
		this.doctorPersonId = doctorPersonId;
		this.clinicalSpecialty = clinicalSpecialty;
		this.service = service;
		this.statusId = statusId;
	}

}
