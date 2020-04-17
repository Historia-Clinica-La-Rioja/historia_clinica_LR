package net.pladema.internation.repository.listener;

import net.pladema.auditable.Auditable;
import net.pladema.auditable.CreationableEntity;
import net.pladema.auditable.DeleteableEntity;
import net.pladema.auditable.UpdateableEntity;
import net.pladema.auditable.entity.Audit;
import net.pladema.auditable.entity.Creationable;
import net.pladema.auditable.entity.Updateable;

import javax.persistence.Embedded;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Optional;

@MappedSuperclass
public abstract class InternationAuditableEntity implements CreationableEntity<Integer>, UpdateableEntity<Integer>, Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2446654484732250647L;

	@Embedded
	private Creationable creationable = new Creationable();

	@Embedded
	private Updateable updateable = new Updateable();

	@Override
	public Integer getCreatedBy() {
		if (creationable != null)
			return creationable.getCreatedBy();
		return null;
	}

	@Override
	public void setCreatedBy(Integer user) {
		if (creationable == null)
			creationable = new Creationable();
		creationable.setCreatedBy(user);
	}

	@Override
	public LocalDateTime getCreatedOn() {
		if (creationable != null)
			return creationable.getCreatedOn();
		return null;
	}

	@Override
	public void setCreatedOn(LocalDateTime dateTime) {
		if (creationable == null)
			creationable = new Creationable();
		creationable.setCreatedOn(dateTime);
	}


	@Override
	public Integer getModifiedBy() {
		if (updateable != null)
			return updateable.getModifiedBy();
		return null;
	}

	@Override
	public void setModifiedBy(Integer user) {
		if (updateable == null)
			updateable = new Updateable();
		updateable.setModifiedBy(user);
	}

	@Override
	public LocalDateTime getUpdatedOn() {
		if (updateable != null)
			return updateable.getUpdatedOn();
		return null;
	}

	@Override
	public void setUpdatedOn(LocalDateTime dateTime) {
		if (updateable == null)
			updateable = new Updateable();
		updateable.setUpdatedOn(dateTime);
	}
}
