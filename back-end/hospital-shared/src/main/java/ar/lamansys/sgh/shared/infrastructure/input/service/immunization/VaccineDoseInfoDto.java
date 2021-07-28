package ar.lamansys.sgh.shared.infrastructure.input.service.immunization;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
@Validated
@NoArgsConstructor
public class VaccineDoseInfoDto {

    @NotNull(message = "{value.mandatory}")
    private String description;

    @NotNull(message = "{value.mandatory}")
    private Short order;
    public VaccineDoseInfoDto(String description, Short order) {
        this.description = description;
        this.order = order;
    }
}
