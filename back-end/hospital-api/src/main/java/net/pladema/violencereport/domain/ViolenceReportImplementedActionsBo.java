package net.pladema.violencereport.domain;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ViolenceReportImplementedActionsBo {

	private CoordinationInsideHealthSectorBo coordinationInsideHealthSector;

	private	CoordinationOutsideHealthSectorBo coordinationOutsideHealthSector;

	private Boolean werePreviousEpisodesWithVictimOrKeeper;

	private List<Short> reportPlaceIds;

	private Boolean reportWasDoneByInstitution;

	private List<Short> reportReasonIds;

	private List<Short> institutionReportPlaceIds;

	private String otherInstitutionReportPlace;

	private Boolean wasSexualViolence;

	private List<Short> implementedActionIds;

}
