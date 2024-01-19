package net.pladema.clinichistory.hospitalization.infrastructure.input.rest.dto;

import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.AnthropometricDataDto;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.DiagnosisDto;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;
import javax.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class AnestheticReportDto {

    @Nullable
    private @Valid DiagnosisDto mainDiagnosis;

    @Nullable
    private @Valid List<DiagnosisDto> diagnosis = new ArrayList<>();

    @Nullable
    private @Valid AnthropometricDataDto anthropometricData;

}
