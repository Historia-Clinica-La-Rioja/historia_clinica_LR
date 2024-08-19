package net.pladema.establishment.repository.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "pac_server_image_lvl")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PacServerImageLvl {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "name", nullable = false, length = 20)
	private String name;

	@Column(name = "aetitle", nullable = false, length = 15)
	private String aetitle;

	@Column(name = "domain", nullable = false, length = 50)
	private String domain;

	@Column(name = "port", nullable = false)
	private String port;

	@Column(name = "sector_id", nullable = false)
	private Integer sectorId;

	@Column(name= "local_viewer_url", nullable = true)
	private String localViewerUrl;
}
