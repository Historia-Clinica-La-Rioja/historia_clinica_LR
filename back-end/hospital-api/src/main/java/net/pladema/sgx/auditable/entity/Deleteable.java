package net.pladema.sgx.auditable.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.time.LocalDateTime;

@Embeddable
@Getter
@Setter
public class Deleteable implements Serializable {

	@Column(name = "deleted", nullable = false)
	private Boolean deleted = false;

	@Column(name = "deleted_on")
	private LocalDateTime deletedOn;

	public boolean isDeleted() {
		return deleted;
	}

}