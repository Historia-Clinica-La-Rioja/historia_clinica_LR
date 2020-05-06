package net.pladema.internation.controller.documents.epicrisis.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
public class EpicrisisObservationsDto implements Serializable {

    private String evolutionNote;

    private String studiesSummaryNote;

    private String physicalExamNote;

    private String indicationsNote;

    private String otherNote;

}
