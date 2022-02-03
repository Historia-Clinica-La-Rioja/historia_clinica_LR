package net.pladema.snowstorm.services.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import net.pladema.snowstorm.repository.domain.SnomedSearchVo;

@AllArgsConstructor
@Getter
@ToString
public class SnomedCachedSearchBo {

    private Integer snomedId;

    private String sctid;

    private String pt;

    public SnomedCachedSearchBo(SnomedSearchVo snomedSearchVo) {
        this.snomedId = snomedSearchVo.getSnomedId();
        this.sctid = snomedSearchVo.getSctid();
        this.pt = snomedSearchVo.getPt();
    }

}
