package net.pladema.internation.service.domain.ips;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.pladema.internation.controller.dto.SnomedDto;

import java.io.Serializable;

@Getter
@Setter
@ToString
public abstract class ClinicalTerm implements Serializable {

    private String statusId;

    private SnomedDto snomed;

    private boolean deleted = false;
}
