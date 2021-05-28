package net.pladema.clinichistory.hospitalization.repository.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "internment_episode_status")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class InternmentEpisodeStatus implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3053291021636483828L;
	public static final Short ACTIVE_ID = 1;
	public static final Short INACTIVE_ID = 2;
	public static final String ACTIVE = "1";
	public static final String INACTIVE = "2";

	@Id
	@Column(name = "id")
	private Short id;

	@Column(name = "description", nullable = false, length = 100)
	private String description;
}
