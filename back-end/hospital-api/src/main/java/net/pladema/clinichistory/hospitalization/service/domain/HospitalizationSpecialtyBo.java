package net.pladema.clinichistory.hospitalization.service.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class HospitalizationSpecialtyBo implements Serializable {

    private Integer id;

    private String name;
}
