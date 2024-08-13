package ar.lamansys.sgh.publicapi.sipplus.infrastructure.input.rest.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import net.minidev.json.JSONArray;

import java.util.List;

@Getter
@Builder
@ToString
public class SipPlusUserDto {

	private String id;

	private String userName;

	private String fullName;

	private String countryId;

	private JSONArray roles;

	private List<SipPlusInstitutionDto> institutions;

	private List<SipPlusInstitutionDto> readableInstitutions;

}
