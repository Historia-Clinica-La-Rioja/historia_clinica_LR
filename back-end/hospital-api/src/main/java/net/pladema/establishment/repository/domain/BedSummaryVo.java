package net.pladema.establishment.repository.domain;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.pladema.establishment.repository.entity.Bed;
import net.pladema.establishment.repository.entity.Sector;
import net.pladema.establishment.repository.entity.SectorType;
import net.pladema.staff.repository.domain.ClinicalSpecialtyVo;

@Getter
@Setter
@ToString
public class BedSummaryVo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3859785005602426098L;

	private BedVo bed;
	private SectorSummaryVo sector;
	private LocalDateTime probableDischargeDate;
	private SectorTypeVo sectorType;

	public BedSummaryVo(Bed bed, Sector sector, LocalDateTime probableDischargeDate,
						String careType, String sectorOrganization, String ageGroup, SectorType st,
						Boolean isBlocked) {
		this.bed = new BedVo(bed, isBlocked);
		this.sector = new SectorSummaryVo(sector, careType, sectorOrganization, ageGroup);
		this.probableDischargeDate = Boolean.FALSE.equals(bed.getFree()) ? probableDischargeDate : null;
		this.sectorType = new SectorTypeVo(st.getId(), st.getDescription());
	}

	public void addSpecialty(ClinicalSpecialtyVo clinicalSpecialty) {
		this.sector.addSpecialty(clinicalSpecialty);
	}

	public void addHierarchicalUnit(HierarchicalUnitVo hierarchicalUnitVo) {
		this.sector.addHierarchicalUnit(hierarchicalUnitVo);
	}
}
