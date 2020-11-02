package net.pladema.patient.repository.domain;

import lombok.*;
import net.pladema.patient.service.domain.MedicalCoverageBo;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public abstract class MedicalCoverageVo {

    private Integer id;

    private String name;

    public abstract MedicalCoverageBo newInstance();
}
