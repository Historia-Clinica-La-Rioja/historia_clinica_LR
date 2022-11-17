package ar.lamansys.sgh.clinichistory.domain.hce.summary;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CounterReferenceSummaryBo {

    private Integer id;

    private String clinicalSpecialtyId;

    private String counterReferenceNote;

    private LocalDate performedDate;

    private CHPersonBo professional;

    private List<ReferenceCounterReferenceFileBo> files;

    private List<CounterReferenceProcedureBo> procedures;

	private String institution;

	private String closureType;
}
