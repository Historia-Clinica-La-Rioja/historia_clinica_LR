package net.pladema.snowstorm.repository.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@ToString
@Getter
public class SnomedSearchVo {

    private Integer snomedId;

    private String sctid;

    private String pt;

}
