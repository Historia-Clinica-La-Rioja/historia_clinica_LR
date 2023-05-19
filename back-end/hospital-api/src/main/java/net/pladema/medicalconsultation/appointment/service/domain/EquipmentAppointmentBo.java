package net.pladema.medicalconsultation.appointment.service.domain;

import lombok.*;
import net.pladema.medicalconsultation.appointment.repository.domain.EquipmentAppointmentVo;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

@Getter
@Setter
@ToString
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class EquipmentAppointmentBo {

	private Integer id;

	private Integer patientId;

	private LocalDate date;

	private LocalTime hour;

	private Short appointmentStateId;

	private boolean overturn;

	private Integer patientMedicalCoverageId;

	private Integer snomedId;

	private Short appointmentBlockMotiveId;

	private boolean isProtected;

	public static EquipmentAppointmentBo fromEquipmentAppointmentVo(EquipmentAppointmentVo equipmentAppointmentVo) {
		return EquipmentAppointmentBo.builder()
				.id(equipmentAppointmentVo.getId())
				.patientId(equipmentAppointmentVo.getPatientId())
				.date(equipmentAppointmentVo.getDate())
				.hour(equipmentAppointmentVo.getHour())
				.appointmentStateId(equipmentAppointmentVo.getAppointmentStateId())
				.overturn(equipmentAppointmentVo.isOverturn())
				.patientMedicalCoverageId(equipmentAppointmentVo.getPatientMedicalCoverageId())
				.build();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof EquipmentAppointmentBo)) return false;
		EquipmentAppointmentBo that = (EquipmentAppointmentBo) o;
		return getId().equals(that.getId());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getId());
	}
}
