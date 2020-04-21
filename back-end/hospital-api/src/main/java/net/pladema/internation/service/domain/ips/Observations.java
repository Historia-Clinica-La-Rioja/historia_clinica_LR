package net.pladema.internation.service.domain.ips;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
public class Observations implements Serializable {

    private String presentIllness;

    private String physicalExamination;

    private String procedure;

    private String evolution;

    private String clinicalReport;

    private String extra;
}
