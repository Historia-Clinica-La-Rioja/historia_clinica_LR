package ar.lamansys.sgh.clinichistory.domain.ips;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class DentalActionBo {

    @NotNull
    private SnomedBo snomed;

    private SnomedBo tooth;

    private SnomedBo surface;

    private boolean diagnostic;

    public boolean isProcedure() {
        return !this.diagnostic;
    }

}
