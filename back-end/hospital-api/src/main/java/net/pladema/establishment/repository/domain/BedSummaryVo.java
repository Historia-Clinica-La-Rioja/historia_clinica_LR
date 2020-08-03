package net.pladema.establishment.repository.domain;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.pladema.establishment.repository.entity.Bed;
import net.pladema.establishment.repository.entity.BedCategory;
import net.pladema.establishment.repository.entity.Sector;
import net.pladema.staff.repository.domain.ClinicalSpecialtyVo;
import net.pladema.staff.repository.entity.ClinicalSpecialty;

@Getter
@Setter
@ToString
public class BedSummaryVo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3859785005602426098L;

	private BedVo bed;
	private BedCategoryVo bedCategory;
	private SectorVo sector;
	private ClinicalSpecialtyVo clinicalSpecialty;
	private LocalDateTime probableDischargeDate;

	public BedSummaryVo(Bed bed, BedCategory bedCategory, Sector sector, ClinicalSpecialty clinicalSpecialty,
			LocalDateTime probableDischargeDate) {
		this.bed = new BedVo(bed);
		this.bedCategory = new BedCategoryVo(bedCategory);
		this.sector = new SectorVo(sector);
		this.clinicalSpecialty = new ClinicalSpecialtyVo(clinicalSpecialty);
		this.probableDischargeDate = Boolean.FALSE.equals(bed.getFree()) ? probableDischargeDate : null;
	}

}
