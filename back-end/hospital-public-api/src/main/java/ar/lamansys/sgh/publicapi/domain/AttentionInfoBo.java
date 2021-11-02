package ar.lamansys.sgh.publicapi.domain;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AttentionInfoBo {

    private Integer id;
    private LocalDate attentionDate;
    private SnomedBo speciality;
    private PersonInfoBo patient;
    private CoverageActivityInfoBo coverage;
    private ScopeEnum scope;
    private InternmentBo internmentInfo;
    private ProfessionalBo responsibleDoctor;
}
