package net.pladema.clinichistory.requests.controller.dto;


import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.SnomedDto;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TranscribedServiceRequestDto {

    @NotNull(message = "{value.mandatory}")
    private SnomedDto healthCondition;

    private List<SnomedDto> diagnosticReports = new ArrayList<>();

    @NotNull(message = "{value.mandatory}")
    private String healthcareProfessionalName;

    @NotNull(message = "{value.mandatory}")
    private String institutionName;

    @Nullable
    private String observations;
}
