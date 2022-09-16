package ar.lamansys.refcounterref.domain.counterreference;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class CounterReferenceInfoBo {

    private Integer id;

    private Integer referenceId;

    private Integer patientId;

    private Integer patientMedicalCoverageId;

    private Integer institutionId;

    private Integer clinicalSpecialtyId;

    private LocalDate performedDate;

    private Integer doctorId;

    private boolean billable;

    private List<Integer> fileIds;

	private Short closureTypeId;
}