package ar.lamansys.sgh.publicapi.patient.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class AppointmentsByUserBo {
	private PatientDataBo patientData;
	private List<AppointmentDataBo> appointmentData;

}
