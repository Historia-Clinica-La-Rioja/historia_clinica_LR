package ar.lamansys.sgh.shared.infrastructure.input.service.immunization;

import lombok.EqualsAndHashCode;
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
@EqualsAndHashCode
public class VaccineDoseInfoDto {

    @NotNull(message = "{value.mandatory}")
    @EqualsAndHashCode.Include
    private String description;

    @NotNull(message = "{value.mandatory}")
    @EqualsAndHashCode.Include
    private Short order;

    public VaccineDoseInfoDto(String description, Short order) {
        this.description = description;
        this.order = order;
    }
}
