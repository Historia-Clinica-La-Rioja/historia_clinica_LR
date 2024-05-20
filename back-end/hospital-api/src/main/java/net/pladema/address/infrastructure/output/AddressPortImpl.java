package net.pladema.address.infrastructure.output;

import ar.lamansys.sgh.shared.infrastructure.input.service.SharedAddressPort;
import lombok.AllArgsConstructor;

import net.pladema.address.repository.AddressRepository;

import net.pladema.address.repository.domain.AddressVo;

import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class AddressPortImpl implements SharedAddressPort {

	private AddressRepository addressRepository;

	private final String SEPARATOR = " - ";

	@Override
	public String fetchPatientCompleteAddress(Integer patientId) {
		AddressVo address = addressRepository.findByPatientId(patientId);
		String result = null;
		if (address.getStreet() != null)
			result = parseAddress(address);
		return result;
	}

	private String parseAddress(AddressVo address) {
		StringBuilder builder = new StringBuilder();
		parseStreetAndNumber(address, builder);
		if (address.getApartment() != null)
			parseApartmentNumberAndFloor(address, builder);
		if (address.getCityDescription() != null)
			builder.append(SEPARATOR).append(address.getCityDescription());
		if (address.getProvinceName() != null)
			builder.append(SEPARATOR).append(address.getProvinceName());
		return builder.toString();
	}

	private void parseApartmentNumberAndFloor(AddressVo address, StringBuilder builder) {
		builder.append(SEPARATOR).append(address.getApartment());
		if (address.getFloor() != null)
			builder.append(SEPARATOR).append(address.getFloor());
	}

	private void parseStreetAndNumber(AddressVo address, StringBuilder builder) {
		final String EMPTY_SEPARATOR = " ";
		builder.append(address.getStreet());
		if (address.getNumber() != null)
			builder.append(EMPTY_SEPARATOR).append(address.getNumber());
	}

}
