package ar.lamansys.sgh.shared.infrastructure.input.service.referencecounterreference;

import ar.lamansys.sgh.shared.infrastructure.input.service.SharedSnomedDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.annotation.Nullable;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ReferenceProblemDto implements Serializable {

    private static final long serialVersionUID = -6727536226629890030L;

    @Nullable
    private Integer id;

    @Valid
    @NotNull(message = "{value.mandatory}")
    private SharedSnomedDto snomed;

}