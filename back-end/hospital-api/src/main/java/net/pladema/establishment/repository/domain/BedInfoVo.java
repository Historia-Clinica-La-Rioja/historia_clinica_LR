package net.pladema.establishment.repository.domain;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.pladema.establishment.repository.entity.Bed;
import net.pladema.establishment.repository.entity.Room;
import net.pladema.establishment.repository.entity.Sector;
import net.pladema.patient.repository.domain.PatientVo;
import net.pladema.person.repository.entity.Person;

@Getter
@Setter
@ToString
public class BedInfoVo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3859785005602426098L;

	private BedVo bed;
	private RoomVo room;
	private SectorVo sector;
	private PatientVo patient;
	private LocalDateTime probableDischargeDate;

	public BedInfoVo(Bed bed, Room room, Sector sector,
					 Integer patientId, Person person, String identificationType,
					 LocalDateTime probableDischargeDate) {
		this.bed = new BedVo(bed);
		this.room = new RoomVo(room);
		this.sector = new SectorVo(sector);
		this.patient = Boolean.FALSE.equals(bed.getFree()) ? new PatientVo(patientId, person, identificationType)
				: null;
		this.probableDischargeDate = Boolean.FALSE.equals(bed.getFree()) ? probableDischargeDate : null;
	}

}
