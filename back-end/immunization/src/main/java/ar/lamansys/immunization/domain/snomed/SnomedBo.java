package ar.lamansys.immunization.domain.snomed;

import ar.lamansys.sgx.shared.exceptions.SelfValidating;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class SnomedBo extends SelfValidating<SnomedBo> {

    private Integer id;

    @NotNull(message = "{value.mandatory}")
    @NotEmpty
    @Length(max = 20, message = "{snomed.id.max.value}")
    private String sctid;

    @NotNull(message = "{value.mandatory}")
    @NotEmpty
    @Length(max = 400, message = "{snomed.pt.max.value}")
    private String pt;

    private String parentId;

    private String parentFsn;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SnomedBo)) return false;
        SnomedBo snomedBo = (SnomedBo) o;
        return Objects.equals(getSctid(), snomedBo.getSctid());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getSctid());
    }
}
