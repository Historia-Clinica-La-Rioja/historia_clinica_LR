package net.pladema.address.controller.service;

import net.pladema.address.controller.dto.AddressDto;
import net.pladema.address.controller.mapper.AddressMapper;
import net.pladema.address.controller.service.domain.AddressBo;
import net.pladema.address.repository.entity.Address;
import net.pladema.address.service.AddressService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressExternalServiceImpl implements AddressExternalService {

    private static final Logger LOG = LoggerFactory.getLogger(AddressExternalServiceImpl.class);

    private final AddressMapper addressMapper;

    private final AddressService addressService;

    public AddressExternalServiceImpl(AddressMapper addressMapper, AddressService addressService) {
        super();
        this.addressService = addressService;
        this.addressMapper = addressMapper;
        LOG.debug("{}", "created service");
    }

    @Override
    public AddressDto addAddress(AddressDto addressDto) {
        LOG.debug("Input parameters -> {}", addressDto);
        Address newAddress = addressMapper.fromAddressDto(addressDto);
        LOG.debug("Mapped from AddressDto result -> {}", newAddress);
        newAddress = addressService.addAddress(newAddress);
        AddressDto result = addressMapper.toAddressDto(newAddress);
        LOG.debug("Mapped fromAddress result -> {}", result);
        LOG.debug("Output -> {}", result);
        return result;
    }

    @Override
    public List<AddressDto> getAddressesByIds(List<Integer> addressesIds) {
        List<AddressBo> addressesBo = addressService.getAddressesByIds(addressesIds);
        return addressMapper.fromAddressBoList(addressesBo);
    }
}
