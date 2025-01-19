package net.pladema.establishment.repository.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.pladema.establishment.repository.entity.Sector;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class SectorOfTypeVo {
    private Integer id;
    private String description;
    private Integer institutionId;
    private Integer sectorId;
    private Short sectorTypeId;
    private Short sectorOrganizationId;
    private Short ageGroupId;
    private Short careTypeId;
    private Short hospitalizationTypeId;
    private Boolean informer;
    private Boolean hasDoctorsOffice;

    public SectorOfTypeVo(Sector sector, Boolean hasDoctorsOffice) {
        this.id = sector.getId();
        this.description = sector.getDescription();
		this.institutionId = sector.getInstitutionId();
        this.sectorId = sector.getSectorId();
		this.sectorTypeId = sector.getSectorTypeId();
		this.sectorOrganizationId = sector.getSectorOrganizationId();
        this.ageGroupId = sector.getAgeGroupId();
        this.careTypeId = sector.getCareTypeId();
        this.hospitalizationTypeId = sector.getHospitalizationTypeId();
		this.informer = sector.getInformer();
        this.hasDoctorsOffice = hasDoctorsOffice;
    }
}
