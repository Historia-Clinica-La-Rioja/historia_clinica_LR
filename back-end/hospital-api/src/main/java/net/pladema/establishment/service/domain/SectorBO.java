package net.pladema.establishment.service.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;

@Getter
@Setter
@ToString
public class SectorBO {

	private Integer id;

	private String description;

	private Integer institutionId;

	private Integer sectorId;

	private Short sectorTypeId;

	private Short sectorOrganizationId;

	private Short ageGroupId;

	private Short careTypeId;

	private Short hospitalizationTypeId;

}
