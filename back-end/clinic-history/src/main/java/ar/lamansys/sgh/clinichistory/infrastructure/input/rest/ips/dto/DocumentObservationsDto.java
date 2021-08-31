package ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.annotation.Nullable;
import java.io.Serializable;

@Getter
@Setter
@ToString
public class DocumentObservationsDto implements Serializable {

    @Nullable
    private String otherNote;

    @Nullable
    private String physicalExamNote;

    @Nullable
    private String studiesSummaryNote;

    @Nullable
    private String evolutionNote;

    @Nullable
    private String clinicalImpressionNote;

    @Nullable
    private String currentIllnessNote;

    @Nullable
    private String indicationsNote;
}
