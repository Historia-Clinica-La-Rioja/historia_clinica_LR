package net.pladema.shedlock.entity;


import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "shedlock")
public class ShedLock implements Serializable {

	private static final long serialVersionUID = 6122710448144131651L;

	@Id
	@Column(name = "name")
	private String name;

	@Column(name = "lock_until ")
	private Timestamp lockUntil;

	@Column(name = "locked_at")
	private Timestamp lockedAt;

	@Column(name = "locked_by")
	private String lockedBy;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Timestamp getLockUntil() {
		return lockUntil;
	}

	public void setLockUntil(Timestamp lockUntil) {
		this.lockUntil = lockUntil;
	}

	public Timestamp getLockedAt() {
		return lockedAt;
	}

	public void setLockedAt(Timestamp lockedAt) {
		this.lockedAt = lockedAt;
	}

	public String getLockedBy() {
		return lockedBy;
	}

	public void setLockedBy(String lockedBy) {
		this.lockedBy = lockedBy;
	}
}
