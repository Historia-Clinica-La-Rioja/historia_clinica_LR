package net.pladema.snvs.infrastructure.input.rest.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
public class SnvsSnomedDto implements Serializable {

    @NotNull(message = "{value.mandatory}")
    @NotEmpty(message = "{value.mandatory}")
    @Length(max = 20, message = "{snomed.id.max.value}")
    @EqualsAndHashCode.Include
    private String sctid;

    @NotNull(message = "{value.mandatory}")
    @NotEmpty(message = "{value.mandatory}")
    @Length(max = 400, message = "{snomed.pt.max.value}")
    @EqualsAndHashCode.Include
    private String pt;

    public SnvsSnomedDto(@NotNull(message = "{value.mandatory}") @NotEmpty @Length(max = 20, message = "{snomed.id.max.value}") String sctid,
                         @NotNull(message = "{value.mandatory}") @NotEmpty @Length(max = 400, message = "{snomed.pt.max.value}") String pt) {
        this.sctid = sctid;
        this.pt = pt;
    }

}
