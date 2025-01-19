package net.pladema.clinichistory.requests.servicerequests.infrastructure.input.rest.dto;

import ar.lamansys.sgx.shared.dates.controller.dto.DateTimeDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.establishment.controller.dto.InstitutionBasicInfoDto;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;


@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class StudyAppointmentDto {

    @NotNull(message = "${value.mandatory}")
    private Integer patientId;

    @NotNull(message = "${value.mandatory}")
    private String patientFullName;

    @NotNull(message = "${value.mandatory}")
    private Short statusId;

    @NotNull(message = "${value.mandatory}")
    private DateTimeDto actionTime;

    @Nullable
    private InformerObservationDto informerObservations;

    @NotNull(message = "${value.mandatory}")
    private InstitutionBasicInfoDto completionInstitution;

    @Nullable
    private String technicianObservations;

    @Nullable
    private Integer sizeImage;

    @NotNull(message = "${value.mandatory}")
    private Boolean isAvailableInPACS;

    @Nullable
    private String imageId;

    @Nullable
    private String localViewerUrl;

}
