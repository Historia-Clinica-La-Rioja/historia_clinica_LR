package net.pladema.nominatim.fetchglobalcoordinatesbyaddress.infrastructure.output;

import ar.lamansys.sgx.shared.restclient.services.RestClient;
import ar.lamansys.sgx.shared.restclient.services.RestClientInterface;
import net.pladema.nominatim.fetchglobalcoordinatesbyaddress.application.port.output.FetchNominatimGlobalCoordinatesByAddressPort;
import net.pladema.nominatim.fetchglobalcoordinatesbyaddress.domain.GlobalCoordinatesBo;
import net.pladema.nominatim.fetchglobalcoordinatesbyaddress.domain.NominatimAddressBo;

import net.pladema.nominatim.fetchglobalcoordinatesbyaddress.infrastructure.output.config.NominatimWSConfig;

import org.apache.http.client.utils.URIBuilder;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;

import java.net.URISyntaxException;
import java.util.HashMap;

@Service
public class FetchNominatimGlobalCoordinatesByAddressPortImpl implements FetchNominatimGlobalCoordinatesByAddressPort {

	private final RestClientInterface restClientInterface;

	public FetchNominatimGlobalCoordinatesByAddressPortImpl(RestTemplateBuilder restTemplateBuilder,
															NominatimWSConfig nominatimWSConfig) {
		this.restClientInterface = new RestClient(restTemplateBuilder.build(), nominatimWSConfig);
	}

	@Override
	public GlobalCoordinatesBo run(NominatimAddressBo address) {
		URIBuilder requestUri = parseToRequestUri(address);
		HashMap<String, Object>[] map = new HashMap[1];
		try {
			map = restClientInterface.exchangeGet(requestUri.build().toString(), map.getClass()).getBody();
		}
		catch (Exception e) {
			return null;
		}

		if (map == null || map.length < 1 || map[0] == null)
			return null;

		if (map[0].get("lon") == null || map[0].get("lat") == null)
			return null;

		return new GlobalCoordinatesBo(Double.valueOf((String) map[0].get("lat")), Double.valueOf((String) map[0].get("lon")));
	}

	private URIBuilder parseToRequestUri(NominatimAddressBo address) {
		URIBuilder result = new URIBuilder();
		if (isNotNullNorEmpty(address.getStreetName()) && isNotNullNorEmpty(address.getHouseNumber()))
			result.addParameter("street", address.getHouseNumber() + " " + address.getStreetName());
		else if (isNotNullNorEmpty(address.getStreetName()))
			result.addParameter("street", address.getStreetName());

		if (isNotNullNorEmpty(address.getCityName()))
			result.addParameter("city", address.getCityName());

		if (isNotNullNorEmpty(address.getPostalCode()))
			result.addParameter("postalcode", address.getPostalCode());

		if (isNotNullNorEmpty(address.getStateName()))
			result.addParameter("state", address.getStateName());

		if (isNotNullNorEmpty(address.getCountryName()))
			result.addParameter("country", address.getCountryName());

		result.setParameter("format", "json");

		return result;
	}

	private boolean isNotNullNorEmpty(String string) {
		return string != null && !string.isEmpty();
	}

}
