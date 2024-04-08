package net.pladema.medicalconsultation.appointment.controller.dto;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

import ar.lamansys.sgh.clinichistory.domain.document.OrderImageFileReducedBo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import net.pladema.establishment.controller.dto.InstitutionBasicInfoDto;
import net.pladema.establishment.controller.dto.OrderImageFileInfoDto;

@Builder
@ToString
@Getter
@AllArgsConstructor
public class EquipmentAppointmentListDto {

	private final Integer id;

	private final AppointmentBasicPatientDto patient;

	private final String date;

	private final String hour;

	private final boolean overturn;

	private final Integer healthInsuranceId;

	private final String medicalCoverageName;

	private final String medicalCoverageAffiliateNumber;

	private final Short appointmentStateId;

	private final boolean isProtected;

	private final InstitutionBasicInfoDto derivedTo;

	private final Short reportStatusId;

	private final String studyName;

	private final Integer serviceRequestId;

	private final Integer transcribedServiceRequestId;

	@Nullable
	private List<OrderImageFileInfoDto> transcribedOrderAttachedFiles;

	public void mapTranscribedOrderAttachedFiles(List<OrderImageFileReducedBo> transcribedOrderAttachedFiles) {
		this.transcribedOrderAttachedFiles = transcribedOrderAttachedFiles.stream().map(files -> {
			return new OrderImageFileInfoDto(files.getId(), files.getName());
		}).collect(Collectors.toList());
	}
}
