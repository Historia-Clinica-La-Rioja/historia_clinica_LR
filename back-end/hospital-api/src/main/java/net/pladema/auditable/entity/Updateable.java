package net.pladema.auditable.entity;

import lombok.Getter;
import lombok.Setter;
import net.pladema.auditable.DeleteableEntity;
import net.pladema.auditable.UpdateableEntity;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.time.LocalDateTime;

@Embeddable
@Getter
@Setter
public class Updateable {

	@Column(name = "updated_on")
	private LocalDateTime updatedOn;

	@Column(name = "modified_by")
	private Integer modifiedBy;

}