package net.pladema.medicalconsultation.appointment.controller.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.pladema.clinichistory.requests.medicationrequests.controller.dto.DoctorInfoDto;
import net.pladema.staff.controller.dto.ProfessionalDto;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class AppointmentOrderDetailImageDto {

	private DoctorInfoDto professional;

	private Integer idServiceRequest;

	private String observations;

	private String professionalOrderTranscribed;

	private LocalDateTime creationDate;

	private String healthCondition;
}
