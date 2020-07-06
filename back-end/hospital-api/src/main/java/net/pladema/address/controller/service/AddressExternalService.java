package net.pladema.address.controller.service;

import net.pladema.address.controller.dto.AddressDto;

import java.util.List;

public interface AddressExternalService {

    public AddressDto addAddress(AddressDto address);

    public List<AddressDto> getAddressesByIds(List<Integer> addressesIds);
}
