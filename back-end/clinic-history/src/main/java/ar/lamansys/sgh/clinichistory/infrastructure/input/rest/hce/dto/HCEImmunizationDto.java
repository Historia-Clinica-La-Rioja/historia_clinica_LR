package ar.lamansys.sgh.clinichistory.infrastructure.input.rest.hce.dto;

import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.SnomedDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.ProfessionalInfoDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.immunization.VaccineConditionDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.immunization.VaccineDoseInfoDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.immunization.VaccineSchemeInfoDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.institution.InstitutionInfoDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.validation.annotation.Validated;

import java.io.Serializable;

@Getter
@Setter
@ToString
@Validated
public class HCEImmunizationDto implements Serializable {

    private static final long serialVersionUID = 6092032949244933507L;

    private Integer id;

    private String statusId;

    private SnomedDto snomed;

    private String administrationDate;

    private String note;

    private InstitutionInfoDto institution;

    private VaccineDoseInfoDto dose;

    private VaccineConditionDto condition;

    private VaccineSchemeInfoDto scheme;

    private String lotNumber;

    private ProfessionalInfoDto doctor;
}
