package ar.lamansys.sgh.clinichistory.domain.hce;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hce.entity.HCEPersonalHistoryVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class HCEPersonalHistoryBo extends HCEHealthConditionBo {

    private String type;

    public HCEPersonalHistoryBo(HCEPersonalHistoryVo source) {
        super(source);
        this.type = source.getType();
        this.setCanBeMarkAsError(false);
    }
}
