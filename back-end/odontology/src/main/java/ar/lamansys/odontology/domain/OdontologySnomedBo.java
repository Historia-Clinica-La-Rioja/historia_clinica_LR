package ar.lamansys.odontology.domain;

import lombok.*;
import java.util.Objects;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class OdontologySnomedBo {

    private Integer id;
    private String sctid;
    private String pt;

    public OdontologySnomedBo(Integer id, String pt) {
        this.id = id;
        this.pt = pt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OdontologySnomedBo)) return false;
        OdontologySnomedBo snomedBo = (OdontologySnomedBo) o;
        return Objects.equals(getSctid(), snomedBo.getSctid());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getSctid());
    }
}
