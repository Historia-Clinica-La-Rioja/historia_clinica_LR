package net.pladema.sanitaryresponsibilityarea.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.nominatim.fetchglobalcoordinatesbyaddress.domain.GlobalCoordinatesBo;

import java.time.LocalDate;

@ToString
@Getter
@Setter
@NoArgsConstructor
public class GetPatientCoordinatesByOutpatientConsultationFilterBo {

	private Integer institutionId;

	private GlobalCoordinatesBo mapUpperCorner;

	private GlobalCoordinatesBo mapLowerCorner;

	private LocalDate fromDate;

	private LocalDate toDate;

}
