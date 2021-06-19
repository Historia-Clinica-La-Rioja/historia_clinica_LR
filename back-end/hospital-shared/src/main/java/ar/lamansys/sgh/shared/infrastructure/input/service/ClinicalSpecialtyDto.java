package ar.lamansys.sgh.shared.infrastructure.input.service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class ClinicalSpecialtyDto implements Serializable {

    private Integer id;

    private String name;
}
