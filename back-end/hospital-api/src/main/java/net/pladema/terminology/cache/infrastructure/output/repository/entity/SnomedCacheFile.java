package net.pladema.terminology.cache.infrastructure.output.repository.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "snomed_cache_file")
public class SnomedCacheFile {
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "ecl", length = 128)
	private String ecl;

	@Column(name = "url", length = 512)
	private String url;

	@Column(name = "created_on", nullable = false)
	private LocalDateTime createdOn;

	@Column(name = "downloaded_on")
	private LocalDateTime downloadedOn;

	@Column(name = "downloaded_error", length = 512)
	private String downloadedError;

	@Column(name = "file_id")
	private Long fileId;

	@Column(name = "ingested_on")
	private LocalDateTime ingestedOn;

	@Column(name = "ingested_error", length = 512)
	private String ingestedError;

	@Column(name = "concepts_loaded")
	private Integer conceptsLoaded;

	@Column(name = "concepts_erroneous")
	private Integer conceptsErroneous;

	public SnomedCacheFile(String ecl, String url) {
		this.ecl = ecl;
		this.url = url;
		this.createdOn = LocalDateTime.now();
	}
}
