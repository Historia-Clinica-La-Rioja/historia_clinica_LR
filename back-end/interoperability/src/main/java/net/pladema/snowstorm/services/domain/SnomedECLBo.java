package net.pladema.snowstorm.services.domain;

import lombok.Getter;
import net.pladema.snowstorm.services.domain.semantics.SnomedECL;

@Getter
public class SnomedECLBo {
    private SnomedECL key;

    private String value;

    public SnomedECLBo(SnomedECL key, String value) {
        this.key = key;
        this.value = value;
    }
}
