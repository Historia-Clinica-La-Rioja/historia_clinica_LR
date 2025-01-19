package net.pladema.establishment.repository.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Objects;

@Entity
@Table(name = "bed")
@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)

public class Bed {
	
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@EqualsAndHashCode.Include
	private Integer id;
	
	@Column(name = "bed_number", nullable = false, unique = true)
	private String bedNumber;
	
	@Column(name = "room_id", nullable = false)
	private Integer roomId;
	
	@Column(name = "bed_category_id", nullable = false)
	private Short bedCategoryId;
	
	@Column(name = "enabled", nullable = false)
	private Boolean enabled;
	
	@Column(name = "available", nullable = false)
	private Boolean available;
	
	@Column(name = "free", nullable = false)
	private Boolean free;

	@Column(name = "incharge_nurse_id")
	private Integer inchargeNurseId;

	@Column(name = "status_id", nullable = true)
	private Integer statusId;

	public void block(Integer newStatusId) {
		this.setStatusId(newStatusId);
		this.setEnabled(false);
		this.setAvailable(false);
		this.setFree(false);
	}

	public void unBlock() {
		this.setStatusId(null);
		this.setEnabled(true);
		this.setAvailable(true);
		this.setFree(true);
	}

	public Boolean statusChanged(Bed update) {
		return !(
			Objects.equals(update.getEnabled(), this.getEnabled()) &&
			Objects.equals(update.getAvailable(), this.getAvailable()) &&
			Objects.equals(update.getFree(), this.getFree())
		);
	}
}
