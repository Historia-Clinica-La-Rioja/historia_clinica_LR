package net.pladema.snowstorm.repository.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "snomed_cache_log")
@Getter
@Setter
@NoArgsConstructor
public class SnomedCacheLog {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "message", length = 1024)
	private String message;

	@Column(name = "created_on", nullable = false)
	private LocalDateTime createdOn;

	public SnomedCacheLog(String message, LocalDateTime createdOn) {
		this.message = message;
		this.createdOn = createdOn;
	}

}
