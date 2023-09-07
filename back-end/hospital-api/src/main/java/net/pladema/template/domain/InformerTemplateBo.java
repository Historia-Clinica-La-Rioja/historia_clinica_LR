package net.pladema.template.domain;

import ar.lamansys.sgh.clinichistory.domain.ips.ConclusionBo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class InformerTemplateBo extends DocumentTemplateBo {

    private List<ConclusionBo> conclusions;

    @Override
    public String toString() {
        return "InformerTemplateBo{" + "conclusions=" + conclusions +
                ", super=" + super.toString() +
                '}';
    }
}
