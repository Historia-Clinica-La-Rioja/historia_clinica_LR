package net.pladema.address.repository.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "province")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Province implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7119645985948251191L;

	@Id
	@Column(name = "id")
	private Short id;

	@Column(name = "description", length = 100, nullable = false)
	private String description;

	@Column(name = "country_id", nullable = false)
	private Short countryId;

}
