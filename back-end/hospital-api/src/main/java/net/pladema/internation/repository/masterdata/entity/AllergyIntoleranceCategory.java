package net.pladema.internation.repository.masterdata.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.auditable.entity.AuditableEntity;
import net.pladema.auditable.listener.AuditListener;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "allergy_intolerance_category")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class AllergyIntoleranceCategory implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3053291021636483828L;

	@Id
	@Column(name = "id", length = 20)
	private String id;

	@Column(name = "description", nullable = false, length = 100)
	private String description;

}
