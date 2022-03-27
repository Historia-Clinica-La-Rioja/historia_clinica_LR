package net.pladema.snowstorm.services.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import net.pladema.snowstorm.repository.domain.SnomedSearchItemVo;

@AllArgsConstructor
@Getter
@ToString
public class SnomedSearchItemBo {

    private Integer snomedId;

    private String sctid;

    private String pt;

    public SnomedSearchItemBo(SnomedSearchItemVo snomedSearchVo) {
        this.snomedId = snomedSearchVo.getSnomedId();
        this.sctid = snomedSearchVo.getSctid();
        this.pt = snomedSearchVo.getPt();
    }

}
