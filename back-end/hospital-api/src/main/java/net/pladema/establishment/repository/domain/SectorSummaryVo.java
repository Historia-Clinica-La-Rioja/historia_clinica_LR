package net.pladema.establishment.repository.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.pladema.establishment.repository.entity.Sector;
import net.pladema.staff.repository.domain.ClinicalSpecialtyVo;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
public class SectorSummaryVo {

    private Integer id;

    private String description;

    private String careType;

    private String organizationType;

    private String ageGroup;

    private List<ClinicalSpecialtyVo> clinicalSpecialties;

	private List<HierarchicalUnitVo> hierarchicalUnit;

    public SectorSummaryVo(Sector sector, String careType, String sectorOrganization, String ageGroup) {
        this.id = sector.getId();
        this.description = sector.getDescription();
        this.careType = careType;
        this.organizationType = sectorOrganization;
        this.ageGroup = ageGroup;
        this.clinicalSpecialties = new ArrayList<>();
		this.hierarchicalUnit = new ArrayList<>();
    }

    public void addSpecialty(ClinicalSpecialtyVo clinicalSpecialty) {
		if (this.clinicalSpecialties.stream().noneMatch(clinicalSpecialtyVo -> clinicalSpecialtyVo.getId().equals(clinicalSpecialty.getId())))
			this.clinicalSpecialties.add(clinicalSpecialty);
    }

	public void addHierarchicalUnit(HierarchicalUnitVo hierarchicalUnit) {
		if (this.hierarchicalUnit.stream().noneMatch(hierarchicalUnitVo -> hierarchicalUnitVo.getId().equals(hierarchicalUnit.getId())))
			this.hierarchicalUnit.add(hierarchicalUnit);
	}

}
