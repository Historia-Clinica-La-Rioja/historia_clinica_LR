package net.pladema.medicalconsultation.appointment.controller.dto;


import ar.lamansys.refcounterref.domain.enums.EReferenceClosureType;
import javax.annotation.Nullable;
import ar.lamansys.sgx.shared.dates.controller.dto.DateTimeDto;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.clinichistory.requests.transcribed.infrastructure.input.rest.dto.TranscribedServiceRequestSummaryDto;
import net.pladema.medicalconsultation.appointment.domain.enums.EPatientIdentityAccreditationStatus;
import net.pladema.medicalconsultation.diary.controller.dto.DiaryLabelDto;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentDto extends CreateAppointmentDto {


	private Integer id;

    private Short appointmentStateId;

    @Nullable
    private String stateChangeReason;

	@Nullable
	private String observation;

	@Nullable
	private String observationBy;

	private boolean isProtected;

	private String callLink;

	@Nullable
	private DiaryLabelDto diaryLabelDto;
	
	private boolean hasAssociatedReference;

	private EReferenceClosureType associatedReferenceClosureType;

	private RecurringTypeDto recurringTypeDto;

	@Nullable
	private boolean hasAppointmentChilds;

	@Nullable
	private Integer parentAppointmentId;

	@Nullable
	private TranscribedServiceRequestSummaryDto transcribedOrderData;
	
	private DateTimeDto updatedOn;

	private EPatientIdentityAccreditationStatus patientIdentityAccreditationStatus;

}
