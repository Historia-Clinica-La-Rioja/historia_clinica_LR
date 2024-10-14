package net.pladema.emergencycare.infrastructure.input.rest.dto;

import ar.lamansys.sgx.shared.dates.controller.dto.DateTimeDto;
import lombok.*;
import net.pladema.establishment.controller.dto.BedDto;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class EmergencyCareDocumentHeaderDto {

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
