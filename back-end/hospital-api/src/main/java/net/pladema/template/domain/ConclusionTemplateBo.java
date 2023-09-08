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

    private String templateText;
    private List<ConclusionBo> conclusions;

    @Override
    public String toString() {
        return "ConclusionTemplateBo{" + "conclusions=" + conclusions +
                ", templateText='" + templateText + '\'' +
                ", super=" + super.toString() +
                '}';
    }
}
