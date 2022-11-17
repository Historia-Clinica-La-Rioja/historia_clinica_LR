package net.pladema.address.service.impl;

import net.pladema.address.controller.service.domain.AddressBo;
import net.pladema.address.repository.AddressRepository;
import net.pladema.address.repository.domain.AddressVo;
import net.pladema.address.repository.entity.Address;
import net.pladema.address.service.AddressService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AddressServiceImpl implements AddressService {

    private static final Logger LOG = LoggerFactory.getLogger(AddressServiceImpl.class);

    private final AddressRepository addressRepository;

    public AddressServiceImpl(AddressRepository addressRepository){
        this.addressRepository = addressRepository;
    }

    @Override
    public Address addAddress(Address address) {
        LOG.debug("Going to save -> {}", address);
        Address addressSaved = addressRepository.save(address);
        LOG.debug("Address saved -> {}", addressSaved);
        return addressSaved;
    }

    @Override
    public List<AddressBo> getAddressesByIds(List<Integer> addressesIds) {
        List<AddressVo> addresses = addressRepository.findByIds(addressesIds);
        return addresses.stream().map(AddressBo::new).collect(Collectors.toList());
    }

	@Override
	public AddressBo getAddressByInstitution(Integer institutionId) {
		return addressRepository.findByInstitutionId(institutionId).orElse(null);
	}

}
