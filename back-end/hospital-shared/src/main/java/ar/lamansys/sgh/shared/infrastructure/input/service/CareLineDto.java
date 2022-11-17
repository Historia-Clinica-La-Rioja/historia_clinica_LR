package ar.lamansys.sgh.shared.infrastructure.input.service;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@ToString
public class CareLineDto implements Serializable {

    private static final long serialVersionUID = -3210933116471323297L;

    private Integer id;

    private String description;

    private List<ClinicalSpecialtyDto> clinicalSpecialties;

}