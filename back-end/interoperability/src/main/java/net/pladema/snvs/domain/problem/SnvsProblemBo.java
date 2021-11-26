package net.pladema.snvs.domain.problem;

import lombok.Getter;
import lombok.ToString;
import net.pladema.snvs.domain.problem.exceptions.SnvsProblemBoEnumException;
import net.pladema.snvs.domain.problem.exceptions.SnvsProblemBoException;

@Getter
@ToString
public class SnvsProblemBo {

    @ToString.Include
    private final String sctid;

    @ToString.Include
    private final String pt;

    public SnvsProblemBo(String sctid, String pt) throws SnvsProblemBoException {
        if (sctid == null)
            throw new SnvsProblemBoException(SnvsProblemBoEnumException.NULL_SCTID,"El código de snomed es obligatoria");
        this.sctid = sctid;
        if (pt == null)
            throw new SnvsProblemBoException(SnvsProblemBoEnumException.NULL_PT,"EL termino preferido del problema es obligatoria");
        this.pt = pt;
    }
}
