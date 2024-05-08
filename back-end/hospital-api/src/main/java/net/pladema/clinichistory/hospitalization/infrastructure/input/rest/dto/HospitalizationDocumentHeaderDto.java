package net.pladema.clinichistory.hospitalization.infrastructure.input.rest.dto;

import ar.lamansys.sgx.shared.dates.controller.dto.DateTimeDto;
import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.establishment.controller.dto.BedDto;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class HospitalizationDocumentHeaderDto {

    @NotNull
    private Long id;

    @NotNull
    private String sourceTypeName;

    @NotNull
    private String clinicalSpecialtyName;

    @NotNull
    private DateTimeDto createdOn;

    @NotNull
    private String professionalName;

    @NotNull
    private String institutionName;

    @Nullable
    private BedDto bed;

}
