package ar.lamansys.sgh.clinichistory.infrastructure.input.rest.hce.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HCEHealthConditionDto extends HCEPersonalHistoryDto {

    private boolean main;

    private String problemId;

    private HCEReferenceDto reference;
}
