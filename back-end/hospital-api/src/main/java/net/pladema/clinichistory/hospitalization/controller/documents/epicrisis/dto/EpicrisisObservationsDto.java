package net.pladema.clinichistory.hospitalization.controller.documents.epicrisis.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.annotation.Nullable;
import java.io.Serializable;

@Getter
@Setter
@ToString
public class EpicrisisObservationsDto implements Serializable {

    @Nullable
    private String evolutionNote;

    @Nullable
    private String studiesSummaryNote;

    @Nullable
    private String physicalExamNote;

    @Nullable
    private String indicationsNote;

    @Nullable
    private String otherNote;

}
