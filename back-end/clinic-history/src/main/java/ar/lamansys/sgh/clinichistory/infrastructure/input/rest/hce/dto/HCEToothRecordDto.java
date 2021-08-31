package ar.lamansys.sgh.clinichistory.infrastructure.input.rest.hce.dto;

import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.SnomedDto;
import ar.lamansys.sgx.shared.dates.controller.dto.DateDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.annotation.Nullable;
import java.io.Serializable;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class HCEToothRecordDto implements Serializable {

    private SnomedDto snomed;

    @Nullable
    private String surfaceSctid;

    private DateDto date;

}
