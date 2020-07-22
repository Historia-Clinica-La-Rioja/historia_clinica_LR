package net.pladema.establishment.repository.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.pladema.clinichistory.hospitalization.repository.listener.InternationAuditableEntity;
import net.pladema.clinichistory.hospitalization.repository.listener.InternationListener;

@Entity
@Table(name = "historic_patient_bed_relocation")
@EntityListeners(InternationListener.class)
@Getter
@Setter
@ToString
public class HistoricPatientBedRelocation extends InternationAuditableEntity{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2434800727451609960L;

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@EqualsAndHashCode.Include
	private Integer id;
	
	@Column(name = "origin_bed_id", nullable = false)
	private Integer originBedId;
	
	@Column(name = "destination_bed_id", nullable = false)
	private Integer destinationBedId;
	
	@Column(name = "internment_episode_id", nullable = false)
	private Integer internmentEpisodeId;
	
	@Column(name = "relocation_date", nullable = false)
	private LocalDateTime relocationDate;
	
	@Column(name = "origin_bed_free", nullable = false)
	private boolean originBedFree = true;
	
}
