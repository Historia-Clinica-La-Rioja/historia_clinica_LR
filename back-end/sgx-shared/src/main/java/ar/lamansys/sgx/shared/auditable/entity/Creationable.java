package ar.lamansys.sgx.shared.auditable.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.time.LocalDateTime;

@Embeddable
@Getter
@Setter
public class Creationable implements Serializable {

	@Column(name = "created_on")
	private LocalDateTime createdOn;

	@Column(name = "created_by")
	private Integer createdBy;

}