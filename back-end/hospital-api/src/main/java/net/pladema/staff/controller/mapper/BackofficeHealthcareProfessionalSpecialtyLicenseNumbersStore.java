package net.pladema.staff.controller.mapper;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import net.pladema.sgx.backoffice.repository.BackofficeStore;
import net.pladema.staff.controller.dto.ProfessionalLicenseNumberDto;
import net.pladema.staff.repository.ProfessionalLicenseNumberRepository;
import net.pladema.staff.repository.entity.ProfessionalLicenseNumber;

@Service
public class BackofficeHealthcareProfessionalSpecialtyLicenseNumbersStore implements BackofficeStore<ProfessionalLicenseNumberDto, Integer> {

    private final ProfessionalLicenseNumberRepository professionalLicenseNumberRepository;

	public BackofficeHealthcareProfessionalSpecialtyLicenseNumbersStore(ProfessionalLicenseNumberRepository professionalLicenseNumberRepository) {
		this.professionalLicenseNumberRepository = professionalLicenseNumberRepository;
	}

	@Override
    public Page<ProfessionalLicenseNumberDto> findAll(ProfessionalLicenseNumberDto example, Pageable pageable) {
		example.setProfessionalProfessionId(null);
		return professionalLicenseNumberRepository.findAll(
                Example.of(fromDto(example)),
                PageRequest.of(
                        pageable.getPageNumber(),
                        pageable.getPageSize(),
                        Sort.unsorted()
                ))
				.map(this::fromEntity);
    }

    @Override
    public List<ProfessionalLicenseNumberDto> findAll() {
        return professionalLicenseNumberRepository.findAllHealthProfessionalSpecialtyLicenseNumbers()
				.stream()
				.map(this::fromEntity)
				.collect(Collectors.toList());
    }

    @Override
    public List<ProfessionalLicenseNumberDto> findAllById(List<Integer> ids) {
        return professionalLicenseNumberRepository.findAllById(ids)
				.stream()
				.filter(professionalLicenseNumber -> professionalLicenseNumber.getProfessionalProfessionId() == null)
				.map(this::fromEntity)
				.collect(Collectors.toList());
    }


    @Override
    public Optional<ProfessionalLicenseNumberDto> findById(Integer id) {
        return professionalLicenseNumberRepository.findById(id)
				.map(this::fromEntity);
    }

    @Override
    public ProfessionalLicenseNumberDto save(ProfessionalLicenseNumberDto dto) {
		dto.setProfessionalProfessionId(null);
		return fromEntity(professionalLicenseNumberRepository.save(fromDto(dto)));
    }

	@Override
    public void deleteById(Integer id) {
		professionalLicenseNumberRepository.deleteById(id);
    }

    @Override
    public Example<ProfessionalLicenseNumberDto> buildExample(ProfessionalLicenseNumberDto entity) {
        return Example.of(entity);
    }

	private ProfessionalLicenseNumberDto fromEntity(ProfessionalLicenseNumber entity) {
		return new ProfessionalLicenseNumberDto(entity.getId(), entity.getLicenseNumber(), entity.getType().getId(),
				entity.getProfessionalProfessionId(), entity.getHealthcareProfessionalSpecialtyId());
	}

	private ProfessionalLicenseNumber fromDto(ProfessionalLicenseNumberDto entity) {
		return new ProfessionalLicenseNumber(entity.getId(), entity.getLicenseNumber(), entity.getTypeId(),
				entity.getProfessionalProfessionId(), entity.getHealthcareProfessionalSpecialtyId());
	}
}
