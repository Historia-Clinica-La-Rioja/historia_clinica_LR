package ar.lamansys.sgh.clinichistory.domain.hce.summary;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;


@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ReferenceSummaryBo {

    private Integer id;

    private String careLine;

    private List<String> clinicalSpecialties;

    private String note;

    private List<ReferenceCounterReferenceFileBo> files;

    private CounterReferenceSummaryBo counterReference;

	private String destinationInstitutionName;

    private Boolean cancelled;

    public ReferenceSummaryBo(Integer id, String careLine, String note, String destinationInstitutionName, Boolean cancelled) {
        this.id = id;
        this.careLine = careLine;
        this.note = note;
		this.destinationInstitutionName = destinationInstitutionName;
        this.cancelled = cancelled;
    }

	public ReferenceSummaryBo(Integer id, String careLine, List<String> clinicalSpecialties, String note, String destinationInstitutionName, Boolean cancelled) {
		this.id = id;
		this.careLine = careLine;
		this.clinicalSpecialties = clinicalSpecialties;
		this.note = note;
		this.destinationInstitutionName = destinationInstitutionName;
		this.cancelled = cancelled;
	}

}
