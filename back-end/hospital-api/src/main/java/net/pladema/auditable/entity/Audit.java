package net.pladema.auditable.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
@Setter
public class Audit implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(name = "created_on")
	private LocalDateTime createdOn;

	@Column(name = "updated_on")
	private LocalDateTime updatedOn;

	@Column(name = "deleted", nullable = false)
	private Boolean deleted = false;

	@Column(name = "delete_date_time")
	private LocalDateTime deleteDateTime;

	public boolean isDeleted() {
		return deleted;
	}

}