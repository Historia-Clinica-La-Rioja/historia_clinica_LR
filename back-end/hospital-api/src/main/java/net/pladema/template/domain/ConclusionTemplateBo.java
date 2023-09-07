package net.pladema.template.domain;

import ar.lamansys.sgh.clinichistory.domain.ips.ConclusionBo;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@JsonIncludeProperties({ "templateText", "conclusions" })
public class ConclusionTemplateBo extends DocumentTemplateBo {

    private List<ConclusionBo> conclusions;

    @Override
    public String toString() {
        return "ConclusionTemplateBo{" + "conclusions=" + conclusions +
                ", super=" + super.toString() +
                '}';
    }
}
