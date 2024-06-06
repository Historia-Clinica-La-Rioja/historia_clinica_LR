package net.pladema.medicalconsultation.appointment.controller.dto;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

import ar.lamansys.sgh.clinichistory.domain.document.OrderImageFileReducedBo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.establishment.controller.dto.InstitutionBasicInfoDto;
import net.pladema.establishment.controller.dto.OrderImageFileInfoDto;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class EquipmentAppointmentListDto {

	private Integer id;

	private AppointmentBasicPatientDto patient;

	private String date;

	private String hour;

	private boolean overturn;

	private Integer healthInsuranceId;

	private String medicalCoverageName;

	private String medicalCoverageAffiliateNumber;

	private Short appointmentStateId;

	private boolean isProtected;

	private InstitutionBasicInfoDto derivedTo;

	private Short reportStatusId;

	@Deprecated
	private String studyName;

	@Nullable
	private List<String> studies;

	private Integer serviceRequestId;

	private Integer transcribedServiceRequestId;

	@Nullable
	private List<OrderImageFileInfoDto> transcribedOrderAttachedFiles;

	public void mapTranscribedOrderAttachedFiles(List<OrderImageFileReducedBo> transcribedOrderAttachedFiles) {
		this.transcribedOrderAttachedFiles = transcribedOrderAttachedFiles.stream().map(files -> {
			return new OrderImageFileInfoDto(files.getId(), files.getName());
		}).collect(Collectors.toList());
	}
}
