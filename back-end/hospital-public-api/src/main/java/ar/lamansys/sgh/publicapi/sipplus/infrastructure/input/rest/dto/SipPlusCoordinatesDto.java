package ar.lamansys.sgh.publicapi.sipplus.infrastructure.input.rest.dto;

import ar.lamansys.sgh.publicapi.sipplus.domain.SipPlusCoordinatesBo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SipPlusCoordinatesDto {

	private String form;

	private String embedId;

	private SipPlusMotherIdentificationDto motherIdentification;

	private Boolean ignoreLocks;

	private Integer pregnancy;

	public SipPlusCoordinatesDto(SipPlusCoordinatesBo sipPlusCoordinatesBo) {
		SipPlusMotherIdentificationDto sipPlusMotherIdentificationDto = SipPlusMotherIdentificationDto.builder()
				.countryCode(sipPlusCoordinatesBo.getMotherIdentification().getCountryCode())
				.typeCode(sipPlusCoordinatesBo.getMotherIdentification().getTypeCode())
				.number(sipPlusCoordinatesBo.getMotherIdentification().getNumber())
				.build();
		this.form = sipPlusCoordinatesBo.getForm();
		this.embedId = sipPlusCoordinatesBo.getEmbedId();
		this.motherIdentification = sipPlusMotherIdentificationDto;
		this.ignoreLocks = sipPlusCoordinatesBo.getIgnoreLocks();
		this.pregnancy = sipPlusCoordinatesBo.getPregnancy();
	}

}
