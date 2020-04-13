package net.pladema.auditable.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

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

	@Column(name = "deleted_on")
	private LocalDateTime deletedOn;

	@Column(name = "created_by")
	private Integer createdBy;

	@Column(name = "modified_by")
	private Integer modifiedBy;

	public boolean isDeleted() {
		return deleted;
	}

}