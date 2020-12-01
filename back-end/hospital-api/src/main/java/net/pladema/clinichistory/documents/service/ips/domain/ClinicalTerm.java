package net.pladema.clinichistory.documents.service.ips.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public abstract class ClinicalTerm {

    private Integer id;

    private String statusId ;

    private SnomedBo snomed;

    private String status;




}
