package ar.lamansys.sgh.clinichistory.infrastructure.input.rest.hce.dto;

import ar.lamansys.sgh.shared.infrastructure.input.service.referencecounterreference.ReferenceCounterReferenceFileDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.annotation.Nullable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HCEReferenceDto {

    private Integer id;

    private String careLine;

    private List<String> clinicalSpecialties;

    private String note;
    
    private List<ReferenceCounterReferenceFileDto> files;

    private HCESummaryCounterReferenceDto counterReference;

	private String destinationInstitutionName;

    @Nullable
    private Boolean cancelled;
}
