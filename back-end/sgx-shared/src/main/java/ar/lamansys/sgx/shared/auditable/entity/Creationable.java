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
public class Creationable implements Serializable {

	@Column(name = "created_on")
	@ToString.Include
	private LocalDateTime createdOn;

	@Column(name = "created_by")
	@ToString.Include
	private Integer createdBy;

}