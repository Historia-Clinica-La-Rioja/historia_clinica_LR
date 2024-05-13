package net.pladema.medicalconsultation.appointment.service.domain;

import ar.lamansys.sgh.clinichistory.domain.ips.DiagnosticReportBo;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.medicalconsultation.appointment.infraestructure.output.repository.appointment.RecurringAppointmentType;
import net.pladema.clinichistory.requests.servicerequests.service.domain.TranscribedServiceRequestBo;
import net.pladema.medicalconsultation.appointment.repository.domain.AppointmentDiaryVo;
import net.pladema.medicalconsultation.appointment.repository.domain.AppointmentVo;
import net.pladema.medicalconsultation.appointment.repository.entity.Appointment;
import net.pladema.medicalconsultation.diary.service.domain.DiaryLabelBo;
import net.pladema.medicalconsultation.diary.service.domain.ProfessionalPersonBo;

import javax.annotation.Nullable;

@Getter
@Setter
@ToString
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class AppointmentBo {

	private Integer id;

	private Integer diaryId;

	private Integer patientId;

	private LocalDate date;

	private LocalTime hour;

	private Short appointmentStateId;

	private boolean overturn;

	private Integer openingHoursId;

	private Integer patientMedicalCoverageId;

	private Short medicalAttentionTypeId;

	private String stateChangeReason;

	private String phonePrefix;

	private String phoneNumber;

	private Integer snomedId;

	private String observation;

	private Integer observationBy;

	private Short appointmentBlockMotiveId;

	private LocalDateTime updatedOn;

	private boolean isProtected;

	private LocalDateTime createdOn;

	private ProfessionalPersonBo professionalPersonBo;

	private String patientEmail;

	private String callId;

	private Short modalityId;
	
	private DiagnosticReportBo orderData;

	private TranscribedServiceRequestBo transcribedOrderData;

	private String applicantHealthcareProfessionalEmail;

	private DiaryLabelBo diaryLabelBo;
	
	private boolean hasAssociatedReference;

	private Short associatedReferenceClosureTypeId;
	
	private RecurringTypeBo recurringTypeBo;

	private Integer parentAppointmentId;

	@Nullable
	private Short appointmentOptionId;

	@Nullable
	private boolean hasAppointmentChilds;

	private Integer referenceId;

	private Short expiredReasonId;

	private String expiredReasonText;

	private boolean expiredRegister;

	public AppointmentBo(Integer diaryId, Integer patientId, LocalDate date, LocalTime hour, Short modalityId, String patientEmail, String callId,
						String applicantHealthcareProfessionalEmail) {
		this.diaryId = diaryId;
		this.patientId = patientId;
		this.date = date;
		this.hour = hour;
		this.modalityId = modalityId;
		this.patientEmail = patientEmail;
		this.callId = callId;
		this.applicantHealthcareProfessionalEmail = applicantHealthcareProfessionalEmail;
	}

	public AppointmentBo(Integer diaryId, Integer patientId, LocalDate date, LocalTime hour, Integer openingHoursId,
						 boolean isOverturn, Integer patientMedicalCoverageId, String phonePrefix, String phoneNumber, Short modalityId) {
		this.diaryId = diaryId;
		this.patientId = patientId;
		this.date = date;
		this.hour = hour;
		this.openingHoursId = openingHoursId;
		this.overturn = isOverturn;
		this.patientMedicalCoverageId = patientMedicalCoverageId;
		this.phonePrefix = phonePrefix;
		this.phoneNumber = phoneNumber;
		this.modalityId = modalityId;
	}

	public static AppointmentBo fromAppointmentDiaryVo(AppointmentDiaryVo appointmentDiaryVo) {
		return AppointmentBo.builder()
				.id(appointmentDiaryVo.getId())
				.diaryId(appointmentDiaryVo.getDiaryId())
				.patientId(appointmentDiaryVo.getPatientId())
				.date(appointmentDiaryVo.getDate())
				.hour(appointmentDiaryVo.getHour())
				.appointmentStateId(appointmentDiaryVo.getAppointmentStateId())
				.overturn(appointmentDiaryVo.isOverturn())
				.patientMedicalCoverageId(appointmentDiaryVo.getPatientMedicalCoverageId())
				.medicalAttentionTypeId(appointmentDiaryVo.getMedicalAttentionTypeId())
				.phonePrefix(appointmentDiaryVo.getPhonePrefix())
				.phoneNumber(appointmentDiaryVo.getPhoneNumber())
				.appointmentBlockMotiveId(appointmentDiaryVo.getAppointmentBlockMotiveId())
				.createdOn(appointmentDiaryVo.getCreatedOn())
				.updatedOn(appointmentDiaryVo.getUpdatedOn())
				.professionalPersonBo(appointmentDiaryVo.getProfessionalPersonVo() != null ? new ProfessionalPersonBo(appointmentDiaryVo.getProfessionalPersonVo()) : null)
				.patientEmail(appointmentDiaryVo.getEmail())
				.diaryLabelBo(appointmentDiaryVo.getDiaryLabel() != null ? new DiaryLabelBo(appointmentDiaryVo.getDiaryLabel()): null)
				.openingHoursId(appointmentDiaryVo.getOpeningHoursId())
				.build();
	}

	public static AppointmentBo fromAppointmentVo(AppointmentVo appointmentVo) {
		return AppointmentBo.builder()
				.id(appointmentVo.getId())
				.patientId(appointmentVo.getPatientId())
				.date(appointmentVo.getDate())
				.hour(appointmentVo.getHour())
				.appointmentStateId(appointmentVo.getAppointmentStateId())
				.overturn(appointmentVo.isOverturn())
				.patientMedicalCoverageId(appointmentVo.getPatientMedicalCoverageId())
				.medicalAttentionTypeId(appointmentVo.getMedicalAttentionTypeId())
				.stateChangeReason(appointmentVo.getStateChangeReason())
				.diaryId(appointmentVo.getDiaryId())
				.observation(appointmentVo.getObservation())
				.observationBy(appointmentVo.getObservationBy())
				.callId(appointmentVo.getCallId())
				.modalityId(appointmentVo.getModalityId())
				.diaryLabelBo(appointmentVo.getDiaryLabel() != null ? new DiaryLabelBo(appointmentVo.getDiaryLabel()): null)
				.patientEmail(appointmentVo.getPatientEmail())
				.recurringTypeBo(appointmentVo.getRecurringAppointmentTypeId() != null
						? new RecurringTypeBo(
							appointmentVo.getRecurringAppointmentTypeId(),
							RecurringAppointmentType.map(appointmentVo.getRecurringAppointmentTypeId()).getValue()
						)
						: null
				)
				.parentAppointmentId(appointmentVo.getAppointment().getParentAppointmentId())
				.updatedOn(appointmentVo.getAppointment().getUpdatedOn())
				.build();
	}

	public static AppointmentBo newFromAppointment(Appointment appointment) {
		return AppointmentBo.builder()
				.id(appointment.getId())
				.patientId(appointment.getPatientId())
				.date(appointment.getDateTypeId())
				.hour(appointment.getHour())
				.appointmentStateId(appointment.getAppointmentStateId())
				.overturn(appointment.getIsOverturn())
				.patientMedicalCoverageId(appointment.getPatientMedicalCoverageId())
				.phonePrefix(appointment.getPhonePrefix())
				.phoneNumber(appointment.getPhoneNumber())
				.snomedId(appointment.getSnomedId())
				.updatedOn(appointment.getUpdatedOn())
				.build();
    }

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof AppointmentBo)) return false;
		AppointmentBo that = (AppointmentBo) o;
		return getId().equals(that.getId());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getId());
	}

	public boolean isExpiredRegister() {
		return LocalDateTime.of(this.date, this.hour).isBefore(this.createdOn.minusHours(3));
	}

}
