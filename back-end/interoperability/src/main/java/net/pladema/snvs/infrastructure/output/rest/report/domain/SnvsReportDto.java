package net.pladema.snvs.infrastructure.output.rest.report.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class SnvsReportDto {

    private SnvsCitizenDto ciudadano;

    private SnvsNominalCaseEventDto eventoCasoNominal;

}
