package net.pladema.staff.controller.mapper;

import net.pladema.sgx.backoffice.repository.BackofficeStore;
import net.pladema.staff.controller.dto.LicenseNumberTypeDto;
import net.pladema.staff.service.domain.ELicenseNumberTypeBo;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class BackofficeLicenseNumberTypeStore implements BackofficeStore<LicenseNumberTypeDto, Short> {


	public BackofficeLicenseNumberTypeStore() {
	}

    private List<LicenseNumberTypeDto> getLicenseNumberTypes() {
        return Stream.of(ELicenseNumberTypeBo.values())
                .map(tr-> new LicenseNumberTypeDto(tr.getId(), tr.getValue()))
                .collect(Collectors.toList());
    }

	@Override
    public Page<LicenseNumberTypeDto> findAll(LicenseNumberTypeDto example, Pageable pageable) {
        List<LicenseNumberTypeDto> list = getLicenseNumberTypes();
        return new PageImpl<>(list, pageable, list.size());
    }

    @Override
    public List<LicenseNumberTypeDto> findAll() {
        return getLicenseNumberTypes();
    }

    @Override
    public List<LicenseNumberTypeDto> findAllById(List<Short> ids) {
        return getLicenseNumberTypes()
				.stream()
				.filter(licenseNumberTypeDto -> ids.contains(licenseNumberTypeDto.getId()))
				.collect(Collectors.toList());
    }

    @Override
    public Optional<LicenseNumberTypeDto> findById(Short id) {
        return getLicenseNumberTypes()
                .stream()
                .filter(licenseNumberTypeDto -> id.equals(licenseNumberTypeDto.getId()))
                .findFirst();
    }

    @Override
    public LicenseNumberTypeDto save(LicenseNumberTypeDto dto) {
        return null;
    }

	@Override
    public void deleteById(Short id) {
        return;
    }

    @Override
    public Example<LicenseNumberTypeDto> buildExample(LicenseNumberTypeDto entity) {
        return Example.of(entity);
    }

}
