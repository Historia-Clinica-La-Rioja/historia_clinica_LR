package net.pladema.establishment.repository.domain;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.pladema.establishment.repository.entity.Bed;
import net.pladema.establishment.repository.entity.BedCategory;
import net.pladema.establishment.repository.entity.Room;
import net.pladema.establishment.repository.entity.Sector;
import net.pladema.patient.repository.domain.PatientVo;
import net.pladema.person.repository.entity.Person;
import net.pladema.staff.repository.entity.ClinicalSpecialty;

@Getter
@Setter
@ToString
public class BedInfoVo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3859785005602426098L;

	private BedVo bed;
	private BedCategoryVo bedCategory;
	private RoomVo room;
	private SectorVo sector;
	private PatientVo patient;
	private LocalDateTime probableDischargeDate;

	public BedInfoVo(Bed bed, BedCategory bedCategory, Room room, Sector sector,
					 ClinicalSpecialty specialty, Integer patientId, Person person,
					 String identificationType, LocalDateTime probableDischargeDate) {
		this.bed = new BedVo(bed);
		this.bedCategory = new BedCategoryVo(bedCategory);
		this.room = new RoomVo(room);
		this.sector = new SectorVo(sector);
		specialty.fixSpecialtyType();
		this.sector.setSpecialtyName(specialty.getName());
		this.patient = Boolean.FALSE.equals(bed.getFree()) ? new PatientVo(patientId, person, identificationType)
				: null;
		this.probableDischargeDate = Boolean.FALSE.equals(bed.getFree()) ? probableDischargeDate : null;
	}

}
