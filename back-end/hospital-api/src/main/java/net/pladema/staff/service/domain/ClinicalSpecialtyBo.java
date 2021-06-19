package net.pladema.staff.service.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class ClinicalSpecialtyBo implements Serializable {

    private Integer id;

    private String name;
}
