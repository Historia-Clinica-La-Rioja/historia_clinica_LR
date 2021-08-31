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
public class Updateable implements Serializable {

	@Column(name = "updated_on")
	@ToString.Include
	private LocalDateTime updatedOn;

	@Column(name = "updated_by")
	@ToString.Include
	private Integer updatedBy;

}