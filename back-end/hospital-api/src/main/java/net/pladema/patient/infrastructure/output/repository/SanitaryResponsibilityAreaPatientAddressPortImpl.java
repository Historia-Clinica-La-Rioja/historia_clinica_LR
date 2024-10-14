package net.pladema.patient.infrastructure.output.repository;

import lombok.RequiredArgsConstructor;
import net.pladema.address.repository.AddressRepository;
import net.pladema.nominatim.fetchglobalcoordinatesbyaddress.application.port.input.FetchGlobalCoordinatesByAddressPort;
import net.pladema.nominatim.fetchglobalcoordinatesbyaddress.domain.enums.ENominatimResponseCode;
import net.pladema.nominatim.fetchglobalcoordinatesbyaddress.infrastructure.input.dto.NominatimAddressDto;
import net.pladema.nominatim.fetchglobalcoordinatesbyaddress.infrastructure.input.dto.NominatimRequestResponseDto;
import net.pladema.patient.application.port.output.SanitaryResponsibilityAreaPatientAddressPort;
import net.pladema.patient.domain.FetchGlobalCoordinatesSanitaryResponsibilityAreaPatientAddressBo;

import net.pladema.patient.domain.PatientGlobalCoordinatesBo;
import net.pladema.patient.infrastructure.output.repository.entity.GeographicallyLocatedPatient;
import net.pladema.patient.repository.PatientRepository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class SanitaryResponsibilityAreaPatientAddressPortImpl implements SanitaryResponsibilityAreaPatientAddressPort {

	private final PatientRepository patientRepository;

	private final FetchGlobalCoordinatesByAddressPort fetchGlobalCoordinatesByAddressPort;

	private final AddressRepository addressRepository;

	private final GeographicallyLocatedPatientRepository geographicallyLocatedPatientRepository;

	@Override
	public Optional<FetchGlobalCoordinatesSanitaryResponsibilityAreaPatientAddressBo> getNonCompletePatientAddress() {
		Pageable pageable = PageRequest.of(0, 1);
		return patientRepository.fetchPatientWithNoGlobalCoordinates(pageable).stream().findFirst();
	}

	@Override
	public PatientGlobalCoordinatesBo fetchPatientGlobalCoordinatesByAddress(FetchGlobalCoordinatesSanitaryResponsibilityAreaPatientAddressBo patientAddress) {
		NominatimAddressDto address = fromFetchGlobalCoordinatesSanitaryResponsibilityAreaPatientAddressBo(patientAddress);
		NominatimRequestResponseDto globalCoordinates = fetchGlobalCoordinatesByAddressPort.run(address);
		return parseToPatientGlobalCoordinates(patientAddress, globalCoordinates);
	}

	private PatientGlobalCoordinatesBo parseToPatientGlobalCoordinates(FetchGlobalCoordinatesSanitaryResponsibilityAreaPatientAddressBo patientAddress, NominatimRequestResponseDto globalCoordinates) {
		PatientGlobalCoordinatesBo result = new PatientGlobalCoordinatesBo(globalCoordinates.getResponseCode(), patientAddress.getPatientId());
		if (globalCoordinates.getGlobalCoordinates() != null)
			setLatitudeAndLongitude(globalCoordinates, result);
		return result;
	}

	private void setLatitudeAndLongitude(NominatimRequestResponseDto globalCoordinates, PatientGlobalCoordinatesBo result) {
		result.setLatitude(globalCoordinates.getGlobalCoordinates().getLatitude());
		result.setLongitude(globalCoordinates.getGlobalCoordinates().getLongitude());
	}

	@Override
	public void handlePatientGlobalCoordinates(PatientGlobalCoordinatesBo patientGlobalCoordinates) {
		if (patientGlobalCoordinates.getNominatimResponseCode().equals(ENominatimResponseCode.SUCCESSFUL))
			addressRepository.savePatientAddressGlobalCoordinates(patientGlobalCoordinates.getPatientId(), patientGlobalCoordinates.getLatitude(), patientGlobalCoordinates.getLongitude());
		geographicallyLocatedPatientRepository.save(new GeographicallyLocatedPatient(patientGlobalCoordinates.getPatientId(), patientGlobalCoordinates.getNominatimResponseCode().getId()));
	}

	private NominatimAddressDto fromFetchGlobalCoordinatesSanitaryResponsibilityAreaPatientAddressBo(FetchGlobalCoordinatesSanitaryResponsibilityAreaPatientAddressBo patientAddress) {
		return NominatimAddressDto.builder()
				.streetName(patientAddress.getStreetName())
				.houseNumber(patientAddress.getHouseNumber())
				.cityName(patientAddress.getCityName())
				.stateName(patientAddress.getStateName())
				.postalCode(patientAddress.getPostalCode())
				.build();
	}

}
