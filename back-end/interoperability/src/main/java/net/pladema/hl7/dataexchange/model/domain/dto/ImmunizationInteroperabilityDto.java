package net.pladema.hl7.dataexchange.model.domain.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import net.pladema.hl7.dataexchange.model.domain.ImmunizationVo;

import java.time.LocalDate;


@Getter
@NoArgsConstructor
public class ImmunizationInteroperabilityDto {

    public ImmunizationInteroperabilityDto(ImmunizationVo immunizationVo){
        this.id = immunizationVo.getId();
        this.vaccineCode = immunizationVo.getVaccineCode();
        this.vaccineTerm = immunizationVo.getVaccineTerm();
        this.status = immunizationVo.getStatus();
        this.immunizationCode = immunizationVo.getImmunizationCode();
        this.immunizationTerm = immunizationVo.getImmunizationTerm();
        this.series = immunizationVo.getSeries();
        this.administrationDate = immunizationVo.getAdministrationDate();
        this.expirationDate = immunizationVo.getExpirationDate();
        this.doseNumber = immunizationVo.getDoseNumber();
        this.primarySource = immunizationVo.isPrimarySource();
        this.batchNumber = immunizationVo.getBatchNumber();
    }

    private String id;

    private String vaccineCode;
    private String vaccineTerm;

    private String status;

    //Immunization info
    private String immunizationCode;
    private String immunizationTerm;

    private String series;

    private LocalDate administrationDate;
    private LocalDate expirationDate;

    private Integer doseNumber;
    private boolean primarySource;
    private String batchNumber;
}
