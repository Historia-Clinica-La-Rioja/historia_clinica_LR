package net.pladema.establishment.controller.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.pladema.clinichistory.hospitalization.controller.dto.ClinicalSpecialtyDto;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class SectorSummaryDto {

    private Integer id;

    private String description;

    private Integer sectorTypeId;

    private String sectorType;

    private Integer careTypeId;

    private String careType;

    private Integer organizationTypeId;

    private String organizationType;

    private Integer ageGroupId;

    private String ageGroup;

    private List<ClinicalSpecialtyDto> clinicalSpecialties;
}
