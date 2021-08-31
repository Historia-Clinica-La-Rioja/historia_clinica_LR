package ar.lamansys.immunization.infrastructure.output.repository.vaccine;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Getter
@Embeddable
@EqualsAndHashCode
public class NomivacSnomedMapPK implements Serializable {

    @Column(name = "sisa_code", nullable = false)
    @EqualsAndHashCode.Include
    private Short sisaCode;

    @Column(name = "sctid", nullable = false, length = 20)
    @EqualsAndHashCode.Include
    private String sctid;

    public NomivacSnomedMapPK(Short sisaCode, String sctid) {
        this.sisaCode = sisaCode;
        this.sctid = sctid;
    }
}
