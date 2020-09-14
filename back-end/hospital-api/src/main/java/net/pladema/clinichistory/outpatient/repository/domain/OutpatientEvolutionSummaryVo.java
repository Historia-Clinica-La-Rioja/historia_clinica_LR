package net.pladema.clinichistory.outpatient.repository.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.pladema.person.repository.entity.Person;
import net.pladema.staff.repository.entity.HealthcareProfessional;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class OutpatientEvolutionSummaryVo implements Serializable {

    private Integer consultationID;

    private List<HealthConditionSummaryVo> healthConditions;

    private LocalDate startDate;

    private List<ReasonSummaryVo> reasons;

    private HealthcareProfessional medic;

    private Person person;

    private List<ProcedureSummaryVo> procedures = new ArrayList<>();

    private String evolutionNote;

    public OutpatientEvolutionSummaryVo(Integer id, LocalDate startDate, HealthcareProfessional medic, Person person, String evolutionNote){
        this.consultationID = id;
        this.startDate = startDate;
        this.medic = medic;
        this.person = person;
        this.evolutionNote = evolutionNote;
    }
}
