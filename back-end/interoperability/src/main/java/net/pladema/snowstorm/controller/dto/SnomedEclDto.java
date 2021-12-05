package net.pladema.snowstorm.controller.dto;

import lombok.Getter;
import net.pladema.snowstorm.services.domain.semantics.SnomedECL;

@Getter
public class SnomedEclDto {

    private SnomedECL key;

    private String value;

    public SnomedEclDto(SnomedECL key, String value) {
        this.key = key;
        this.value = value;
    }
}
