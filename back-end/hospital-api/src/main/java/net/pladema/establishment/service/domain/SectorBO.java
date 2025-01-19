package net.pladema.establishment.service.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

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

	private Boolean informer;

	private Boolean hasDoctorsOffice;

}
