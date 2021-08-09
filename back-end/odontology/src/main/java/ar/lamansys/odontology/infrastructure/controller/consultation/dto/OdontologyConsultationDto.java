package ar.lamansys.odontology.infrastructure.controller.consultation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Nullable;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Validated
public class OdontologyConsultationDto implements Serializable {

    @NotNull(message = "{value.mandatory}")
    private Integer clinicalSpecialtyId;

    @Nullable
    @Valid
    private List<OdontologyDentalActionDto> dentalActions;

}
