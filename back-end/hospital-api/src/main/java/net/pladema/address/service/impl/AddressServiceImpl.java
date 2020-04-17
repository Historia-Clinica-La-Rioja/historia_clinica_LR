package net.pladema.address.service.impl;

import net.pladema.address.repository.AddressRepository;
import net.pladema.address.repository.entity.Address;
import net.pladema.address.service.AddressService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AddressServiceImpl implements AddressService {

    private  final Logger LOG = LoggerFactory.getLogger(this.getClass());

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

}
