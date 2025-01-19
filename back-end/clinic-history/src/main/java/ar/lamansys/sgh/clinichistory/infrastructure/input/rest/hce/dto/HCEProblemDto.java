package ar.lamansys.sgh.clinichistory.infrastructure.input.rest.hce.dto;

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
public class HCEProblemDto extends HCEHealthConditionDto {

    private boolean main;

    private String problemId;

    private List<HCEReferenceDto> references;

    @Nullable
    private Boolean isMarkedAsError;

    @Nullable
    private HCEErrorProblemDto errorProblem;
}
