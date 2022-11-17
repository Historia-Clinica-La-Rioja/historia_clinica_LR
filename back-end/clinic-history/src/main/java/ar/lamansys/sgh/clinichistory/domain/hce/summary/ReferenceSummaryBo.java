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

    private String clinicalSpecialty;

    private String note;

    private List<ReferenceCounterReferenceFileBo> files;

    private CounterReferenceSummaryBo counterReference;

	private String institution;

    public ReferenceSummaryBo(Integer id, String careLine, String clinicalSpecialty, String note, String institution) {
        this.id = id;
        this.careLine = careLine;
        this.clinicalSpecialty = clinicalSpecialty;
        this.note = note;
		this.institution = institution;
    }

}
