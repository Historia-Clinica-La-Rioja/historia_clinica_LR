package net.pladema.address.controller;

import lombok.RequiredArgsConstructor;
import net.pladema.address.controller.dto.AddressDto;
import net.pladema.address.controller.mapper.AddressMapper;
import net.pladema.address.repository.AddressRepository;
import net.pladema.address.repository.CityRepository;
import net.pladema.address.repository.DepartmentRepository;
import net.pladema.establishment.sanitaryresponsibilityarea.institutionresponsibilityarea.infrastructure.output.repository.InstitutionResponsibilityAreaRepository;
import net.pladema.sgx.backoffice.repository.BackofficeStore;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class BackofficeAddressStore implements BackofficeStore<AddressDto, Integer> {
    private final AddressRepository repository;
    private final AddressMapper addressMapper;
    private final DepartmentRepository departmentRepository;
    private final CityRepository cityRepository;
	private final InstitutionResponsibilityAreaRepository institutionResponsibilityAreaRepository;

    @Override
    public Page<AddressDto> findAll(AddressDto example, Pageable pageable) {
        return null;
    }

    @Override
    public List<AddressDto> findAll() {
        return repository.findAll().stream()
                .map(addressMapper::toAddressDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<AddressDto> findAllById(List<Integer> ids) {
        return repository.findAllById(ids).stream()
                .map(addressMapper::toAddressDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<AddressDto> findById(Integer id) {
        Optional<AddressDto> addressDto = repository.findById(id)
                .map(addressMapper::toAddressDto);
        if(addressDto.isPresent()){
            AddressDto ad = addressDto.get();
            ad.setDepartmentId(cityRepository.findDepartmentByCity(ad.getCityId()));
            ad.setProvinceId(departmentRepository.findProvinceByDepartment(ad.getDepartmentId()));
            return Optional.of(ad);
        }
        return addressDto;
    }

    @Override
    public AddressDto save(AddressDto entity) {
		if (entity.getLatitude() == null || entity.getLongitude() == null)
			institutionResponsibilityAreaRepository.deleteByAddressId(entity.getId());
        repository.save(addressMapper.fromAddressDto(entity));
        return entity;
    }

    @Override
    public void deleteById(Integer id) {
        repository.deleteById(id);
    }

    @Override
    public Example<AddressDto> buildExample(AddressDto entity) {
        return null;
    }
}

