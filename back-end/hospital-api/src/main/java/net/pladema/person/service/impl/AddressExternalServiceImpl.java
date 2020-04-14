package net.pladema.person.service.impl;

import net.pladema.address.repository.entity.Address;
import net.pladema.address.service.AddressService;
import net.pladema.address.service.impl.AddressServiceImpl;
import net.pladema.person.service.AddressExternalService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class AddressExternalServiceImpl implements AddressExternalService {

    private static final Logger LOG = LoggerFactory.getLogger(AddressServiceImpl.class);

    private final AddressService addressService;

    public AddressExternalServiceImpl(AddressService addressService) {
        super();
        this.addressService = addressService;
        LOG.debug("{}", "created service");
    }

    @Override
    public Address addAddress(Address address) {
        Address addressSaved = addressService.addAddress(address);
        return addressSaved;
    }
}
