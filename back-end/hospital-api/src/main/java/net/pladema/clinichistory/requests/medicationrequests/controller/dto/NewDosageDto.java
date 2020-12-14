package net.pladema.clinichistory.requests.medicationrequests.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
public class NewDosageDto implements Serializable {

    private Integer frequency;

    private boolean diary;

    private boolean chronic;

    private Integer duration;

}
