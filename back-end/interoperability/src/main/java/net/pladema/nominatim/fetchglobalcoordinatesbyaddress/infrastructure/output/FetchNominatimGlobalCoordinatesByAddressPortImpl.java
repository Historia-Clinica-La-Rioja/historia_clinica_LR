package net.pladema.nominatim.fetchglobalcoordinatesbyaddress.infrastructure.output;

import ar.lamansys.sgx.shared.restclient.configuration.resttemplate.exception.RestTemplateApiException;
import ar.lamansys.sgx.shared.restclient.services.RestClient;
import ar.lamansys.sgx.shared.restclient.services.RestClientInterface;
import net.pladema.nominatim.fetchglobalcoordinatesbyaddress.application.port.output.FetchNominatimGlobalCoordinatesByAddressPort;
import net.pladema.nominatim.fetchglobalcoordinatesbyaddress.domain.GlobalCoordinatesBo;
import net.pladema.nominatim.fetchglobalcoordinatesbyaddress.domain.NominatimAddressBo;

import net.pladema.nominatim.fetchglobalcoordinatesbyaddress.domain.NominatimRequestResponseBo;
import net.pladema.nominatim.fetchglobalcoordinatesbyaddress.domain.enums.ENominatimResponseCode;
import net.pladema.nominatim.fetchglobalcoordinatesbyaddress.infrastructure.output.config.NominatimWSConfig;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class FetchNominatimGlobalCoordinatesByAddressPortImpl implements FetchNominatimGlobalCoordinatesByAddressPort {

	private final RestClientInterface restClientInterface;

	public FetchNominatimGlobalCoordinatesByAddressPortImpl(RestTemplateBuilder restTemplateBuilder,
															NominatimWSConfig nominatimWSConfig) {
		this.restClientInterface = new RestClient(restTemplateBuilder.build(), nominatimWSConfig);
	}

	@Override
	public NominatimRequestResponseBo run(NominatimAddressBo address) {
		String requestString = parseToRequestString(address);
		HashMap<String, Object>[] map = new HashMap[1];
		try {
			map = restClientInterface.exchangeGet(requestString, map.getClass()).getBody();
		}
		catch (RestTemplateApiException e) {
			return new NominatimRequestResponseBo(ENominatimResponseCode.SERVER_ERROR, null);
		}

		if (map == null || map.length < 1 || map[0] == null)
			return new NominatimRequestResponseBo(ENominatimResponseCode.NOT_FOUND, null);

		if (map[0].get("lon") == null || map[0].get("lat") == null)
			return new NominatimRequestResponseBo(ENominatimResponseCode.NOT_FOUND, null);

		return parseResponse(map);
	}

	private NominatimRequestResponseBo parseResponse(HashMap<String, Object>[] map) {
		GlobalCoordinatesBo globalCoordinates = new GlobalCoordinatesBo(Double.valueOf((String) map[0].get("lat")), Double.valueOf((String) map[0].get("lon")));
		return new NominatimRequestResponseBo(ENominatimResponseCode.SUCCESSFUL, globalCoordinates);
	}

	private String parseToRequestString(NominatimAddressBo address) {
		StringBuilder result = new StringBuilder();
		String EMPTY_SPACE = " ";
		String ENCODED_EMPTY_SPACE = "+";

		if (isNotNullNorEmpty(address.getStreetName()) && isNotNullNorEmpty(address.getHouseNumber()))
			result.append("?street=").append(address.getStreetName()).append(EMPTY_SPACE).append(address.getHouseNumber());
		else if (isNotNullNorEmpty(address.getStreetName()))
			result.append("?street=").append(address.getStreetName());

		if (isNotNullNorEmpty(address.getCityName()))
			result.append("&city=").append(address.getCityName());

		if (isNotNullNorEmpty(address.getPostalCode()))
			result.append("&postalcode=").append(address.getPostalCode());

		if (isNotNullNorEmpty(address.getStateName()))
			result.append("&state=").append(address.getStateName());

		if (isNotNullNorEmpty(address.getCountryName()))
			result.append("&country=").append(address.getCountryName());

		result.append("&format=json");

		return result.toString().replace(EMPTY_SPACE, ENCODED_EMPTY_SPACE);
	}

	private boolean isNotNullNorEmpty(String string) {
		return string != null && !string.isEmpty();
	}

}
