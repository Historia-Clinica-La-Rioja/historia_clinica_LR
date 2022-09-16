package ar.lamansys.refcounterref.domain.counterreference;

import ar.lamansys.refcounterref.domain.allergy.CounterReferenceAllergyBo;
import ar.lamansys.refcounterref.domain.medication.CounterReferenceMedicationBo;
import ar.lamansys.refcounterref.domain.procedure.CounterReferenceProcedureBo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
public class CounterReferenceBo {

    private Integer patientId;

    private Integer institutionId;

    private Integer referenceId;

    private Integer clinicalSpecialtyId;

    private Integer patientMedicalCoverageId;

    private String counterReferenceNote;

    private boolean billable;

    private LocalDate performedDate;

    private List<@Valid CounterReferenceProcedureBo> procedures = new ArrayList<>();

    private List<@Valid CounterReferenceMedicationBo> medications = new ArrayList<>();

    private List<@Valid CounterReferenceAllergyBo> allergies = new ArrayList<>();

    private List<Integer> fileIds;

	private Short closureTypeId;
}