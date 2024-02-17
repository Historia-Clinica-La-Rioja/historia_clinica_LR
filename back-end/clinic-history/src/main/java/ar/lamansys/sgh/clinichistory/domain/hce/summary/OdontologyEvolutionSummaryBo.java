package ar.lamansys.sgh.clinichistory.domain.hce.summary;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class OdontologyEvolutionSummaryBo {

    private Integer consultationId;

    private DocumentDataBo document;

    private HealthcareProfessionalBo professional;

    private ClinicalSpecialtyBo clinicalSpecialty;

    private List<HealthConditionSummaryBo> healthConditions;

    private LocalDateTime startDate;

    private List<ReasonSummaryBo> reasons;

    private List<ProcedureSummaryBo> procedures = new ArrayList<>();

    private String evolutionNote;

    public OdontologyEvolutionSummaryBo(Integer id, LocalDateTime startDate,
                                        HealthcareProfessionalBo professional,
                                        ClinicalSpecialtyBo clinicalSpecialty,
                                        String evolutionNote, DocumentDataBo document){
        this.consultationId = id;
        this.clinicalSpecialty = clinicalSpecialty;
        this.startDate = startDate;
        this.professional = professional;
        this.evolutionNote = evolutionNote;
        this.document = document;
    }
}
