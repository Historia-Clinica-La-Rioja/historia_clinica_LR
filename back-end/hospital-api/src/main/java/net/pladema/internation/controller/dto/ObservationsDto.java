package net.pladema.internation.controller.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
public class ObservationsDto implements Serializable {

    private String presentIllness;

    private String physicalExamination;

    private String procedure;

    private String evolution;

    private String clinicalReport;

    private String extra;
}
