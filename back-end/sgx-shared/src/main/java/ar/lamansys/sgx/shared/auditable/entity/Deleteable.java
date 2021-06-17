package ar.lamansys.sgx.shared.auditable.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.time.LocalDateTime;

@Embeddable
@Getter
@Setter
@ToString
public class Deleteable implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1845551268050864290L;

	@Column(name = "deleted", nullable = false)
	@ToString.Include
	private Boolean deleted = false;

	@Column(name = "deleted_on")
	@ToString.Include
	private LocalDateTime deletedOn;

	@Column(name = "deleted_by")
	@ToString.Include
	private Integer deletedBy;

	public boolean isDeleted() {
		return deleted;
	}

}