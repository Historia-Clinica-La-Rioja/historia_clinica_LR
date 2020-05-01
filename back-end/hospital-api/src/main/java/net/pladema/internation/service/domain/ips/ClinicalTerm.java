package net.pladema.internation.service.domain.ips;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.pladema.internation.service.domain.SnomedBo;

import java.io.Serializable;

@Getter
@Setter
@ToString
public abstract class ClinicalTerm implements Serializable {

    private Integer id;

    private String statusId;

    private SnomedBo snomed;
}
