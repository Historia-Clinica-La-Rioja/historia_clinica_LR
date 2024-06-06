package net.pladema.medicalconsultation.appointment.service.domain;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

import ar.lamansys.sgh.clinichistory.domain.document.OrderImageFileReducedBo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.establishment.service.domain.InstitutionBasicInfoBo;
import net.pladema.medicalconsultation.appointment.repository.domain.EquipmentAppointmentVo;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
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

	private InstitutionBasicInfoBo derivedTo;

	private Short reportStatusId;

	private Integer serviceRequestId;

	private Integer transcribedServiceRequestId;

	private List<OrderImageFileReducedBo> transcribedOrderAttachedFiles;

	private Integer diagnosticReportId;

	private List<String> studies;

	public static EquipmentAppointmentBo fromEquipmentAppointmentVo(EquipmentAppointmentVo equipmentAppointmentVo) {
		return EquipmentAppointmentBo.builder()
				.id(equipmentAppointmentVo.getId())
				.patientId(equipmentAppointmentVo.getPatientId())
				.date(equipmentAppointmentVo.getDate())
				.hour(equipmentAppointmentVo.getHour())
				.appointmentStateId(equipmentAppointmentVo.getAppointmentStateId())
				.overturn(equipmentAppointmentVo.isOverturn())
				.patientMedicalCoverageId(equipmentAppointmentVo.getPatientMedicalCoverageId())
				.derivedTo(equipmentAppointmentVo.getInstitutionBasicInfoBo())
				.reportStatusId(equipmentAppointmentVo.getReportStatusId())
				.diagnosticReportId(equipmentAppointmentVo.getDiagnosticReportId())
				.serviceRequestId(equipmentAppointmentVo.getServiceRequestId())
				.transcribedServiceRequestId(equipmentAppointmentVo.getTranscribedServiceRequestId())
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
