package net.pladema.clinichistory.outpatient.createoutpatient.service.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.pladema.clinichistory.ips.service.domain.ClinicalTerm;

import java.time.LocalDate;

@Getter
@Setter
@ToString
public class ProcedureBo extends ClinicalTerm {

    private LocalDate fecha;

}
