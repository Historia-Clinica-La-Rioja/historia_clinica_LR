package net.pladema.address.controller.mapper;

import net.pladema.address.controller.dto.AddressDto;
import net.pladema.address.repository.entity.Address;
import net.pladema.patient.controller.dto.APatientDto;
import net.pladema.patient.controller.dto.BMPatientDto;
import net.pladema.patient.controller.dto.PatientSearchDto;
import net.pladema.patient.service.domain.PatientSearch;
import net.pladema.person.controller.mapper.PersonMapper;
import net.pladema.person.repository.entity.Person;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(uses = {CityMapper.class, ProvinceMapper.class})
public interface AddressMapper {

    public Address fromAddressDto(AddressDto addressDto);

    public AddressDto fromAddress(Address address);
}
