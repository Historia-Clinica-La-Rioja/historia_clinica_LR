package net.pladema.establishment.controller.service.impl;

import java.time.ZoneId;
import java.util.Optional;

import net.pladema.address.controller.service.domain.AddressBo;
import net.pladema.address.service.AddressService;

import org.springframework.stereotype.Service;

import ar.lamansys.sgh.shared.infrastructure.input.service.SharedAddressDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.institution.InstitutionInfoDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.institution.SharedInstitutionPort;
import net.pladema.establishment.controller.service.InstitutionExternalService;
import net.pladema.establishment.service.InstitutionService;
import net.pladema.establishment.service.domain.InstitutionBo;

@Service
public class InstitutionExternalServiceImpl implements InstitutionExternalService, SharedInstitutionPort {

    private final InstitutionService institutionService;

	private final AddressService addressService;

    public InstitutionExternalServiceImpl(InstitutionService institutionService, AddressService addressService) {
        this.institutionService = institutionService;
		this.addressService = addressService;
    }

    @Override
    public ZoneId getTimezone(Integer institutionId) {
        InstitutionBo institutionBo = institutionService.get(institutionId);
        return ZoneId.of(institutionBo.getTimezone());
    }

    @Override
    public InstitutionInfoDto fetchInstitutionById(Integer id) {
		AddressBo addressBo = addressService.getAddressByInstitution(id);
		String address = addressBo.getStreet() + " " + addressBo.getNumber();
        return Optional.ofNullable(institutionService.get(id))
                .map(institutionBo -> new InstitutionInfoDto(institutionBo.getId(), institutionBo.getName(), institutionBo.getSisaCode(), address, institutionBo.getPhone(), institutionBo.getEmail()))
                .orElse(null);
    }

	@Override
	public InstitutionInfoDto fetchInstitutionDataById(Integer id) {
		return Optional.ofNullable(institutionService.get(id))
				.map(institutionBo -> new InstitutionInfoDto(institutionBo.getId(), institutionBo.getName(), institutionBo.getPhone(), institutionBo.getEmail()))
				.orElse(null);
	}

	@Override
	public ar.lamansys.sgh.shared.domain.general.AddressBo fetchInstitutionAddress(Integer id){
		return Optional.ofNullable(institutionService.getInstitutionAddress(id))
				.orElse(null);
	}

	@Override
	public SharedAddressDto fetchAddress(Integer institutionId) {
		var result = Optional.ofNullable(institutionService.getAddress(institutionId));
		return result.map(address -> SharedAddressDto.builder()
						.street(address.getStreet())
						.number(address.getNumber())
						.floor(address.getFloor())
						.apartment(address.getApartment())
						.postCode(address.getPostcode())
						.cityName(address.getCity().getDescription())
						.departmentName(address.getDepartmentName())
						.countryName(address.getCountryName())
						.bahraCode(address.getBahraCode())
						.build())
				.orElse(null);
	}

	@Override
	public InstitutionInfoDto fetchInstitutionBySisaCode(String sisaCode) {
		return Optional.ofNullable(institutionService.get(sisaCode))
				.map(institutionBo -> new InstitutionInfoDto(institutionBo.getId(), institutionBo.getName(), institutionBo.getSisaCode()))
				.orElse(null);
	}

}
