package net.pladema.clinichistory.requests.medicationrequests.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
public class DosageDto implements Serializable {

    private Integer frequency;

    private String periodUnit;

    private Integer duration;

    private String durationUnit;
}
