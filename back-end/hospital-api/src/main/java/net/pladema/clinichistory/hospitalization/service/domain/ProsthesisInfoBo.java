package net.pladema.clinichistory.hospitalization.service.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class ProsthesisInfoBo {

    private Boolean hasProsthesis;

    private String description;

}
