package ar.lamansys.sgh.clinichistory.infrastructure.input.rest.hce.dto;

import javax.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HCEPersonalHistoryDto extends HCEHealthConditionDto {

    @Nullable
    private String type;
}
