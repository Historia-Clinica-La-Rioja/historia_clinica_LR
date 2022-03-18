package ar.lamansys.sgh.clinichistory.domain.ips;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class IndicationBo {

	private Integer id;

	private Integer patientId;

	private Short typeId;

	private Short statusId;

	private String createdByName;

	private Integer createdBy;

	private Integer professionalId;

	private LocalDate indicationDate;

	private LocalDateTime createdOn;

	public IndicationBo(Integer id, Integer patientId, Short typeId, Short statusId, Integer createdBy, Integer professionalId, LocalDate indicationDate, LocalDateTime createdOn){
		this.id = id;
		this.patientId = patientId;
		this.typeId = typeId;
		this.statusId = statusId;
		this.createdBy = createdBy;
		this.professionalId = professionalId;
		this.indicationDate = indicationDate;
		this.createdOn = createdOn;
	}

}
