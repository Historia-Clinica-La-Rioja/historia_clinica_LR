package net.pladema.patient.service.domain;

import lombok.*;
import net.pladema.patient.controller.dto.CoverageDto;
import net.pladema.patient.repository.entity.MedicalCoverage;

import java.io.Serializable;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public abstract class MedicalCoverageBo implements Serializable {

    private Integer id;

    private String name;

    private String cuit;

	private Short type;

    public abstract CoverageDto newInstance();

    public abstract MedicalCoverage mapToEntity();
}
