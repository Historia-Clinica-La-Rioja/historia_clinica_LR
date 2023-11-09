package net.pladema.clinichistory.outpatient.infrastructure.input.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.annotation.Nullable;
import java.util.List;

@Getter
@Setter
@ToString
public class ProblemInfoDto {

    @Nullable
    private List<Integer> diagnosticReportsId;

    @Nullable
    private List<Integer> serviceRequestsId;

    @Nullable
    private List<Integer> appointmentsId;

    @Nullable
    private List<Integer> referencesId;
}
