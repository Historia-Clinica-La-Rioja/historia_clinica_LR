package net.pladema.clinichistory.outpatient.createoutpatient.service.domain.reference;

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

    private ProfessionalInfoBo professional;

    private List<ReferenceCounterReferenceFileBo> files;

    private List<CounterReferenceProcedureBo> procedures;

}
