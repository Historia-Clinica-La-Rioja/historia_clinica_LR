package ar.lamansys.sgx.shared.auditable.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Embedded;
import javax.persistence.MappedSuperclass;

import ar.lamansys.sgx.shared.auditable.CreationableEntity;
import ar.lamansys.sgx.shared.auditable.DeleteableEntity;
import ar.lamansys.sgx.shared.auditable.UpdateableEntity;
import lombok.Getter;
import lombok.ToString;

@MappedSuperclass
@ToString
@Getter
public abstract class SGXAuditableEntity<ID> implements CreationableEntity<Integer>, UpdateableEntity<Integer>, DeleteableEntity<Integer>, Serializable, SGXEntity<ID> {

	/**
	 *
	 */
	private static final long serialVersionUID = 2446654484732250647L;

	@Embedded
	@ToString.Include
	private Creationable creationable;

	@Embedded
	@ToString.Include
	private Updateable updateable;

	@Embedded
	@ToString.Include
	private Deleteable deleteable;

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
	public Integer getUpdatedBy() {
		if (updateable != null)
			return updateable.getUpdatedBy();
		return null;
	}

	@Override
	public void setUpdatedBy(Integer user) {
		if (updateable == null)
			updateable = new Updateable();
		updateable.setUpdatedBy(user);
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


	@Override
	public Integer getDeletedBy() {
		if (deleteable != null)
			return deleteable.getDeletedBy();
		return null;
	}

	@Override
	public void setDeletedBy(Integer user) {
		if (deleteable == null)
			deleteable = new Deleteable();
		deleteable.setDeletedBy(user);
	}

	@Override
	public LocalDateTime getDeletedOn() {
		if (deleteable != null)
			return deleteable.getDeletedOn();
		return null;
	}

	@Override
	public void setDeletedOn(LocalDateTime dateTime) {
		if (deleteable == null)
			deleteable = new Deleteable();
		deleteable.setDeletedOn(dateTime);
	}

	@Override
	public boolean isDeleted() {
		if (deleteable != null)
			return deleteable.isDeleted();
		return false;
	}

	@Override
	public void setDeleted(Boolean deleted) {
		if (deleteable == null)
			deleteable = new Deleteable();
		deleteable.setDeleted(deleted);
	}

	public void initializeAuditableFields() {
		this.creationable = new Creationable();
		this.updateable = new Updateable();
		this.deleteable = new Deleteable();
	}

	public void reactivate() {
		deleteable.setDeleted(false);
		deleteable.setDeletedBy(null);
		deleteable.setDeletedOn(null);
	}
}
