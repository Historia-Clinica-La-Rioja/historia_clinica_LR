package net.pladema.address.service;

import net.pladema.address.controller.service.domain.AddressBo;
import net.pladema.address.repository.entity.Address;

import java.util.List;

public interface AddressService {

    Address addAddress(Address address);

    List<AddressBo> getAddressesByIds(List<Integer> addressesIds);

    AddressBo getAddressByInstitution(Integer institutionId);
}
