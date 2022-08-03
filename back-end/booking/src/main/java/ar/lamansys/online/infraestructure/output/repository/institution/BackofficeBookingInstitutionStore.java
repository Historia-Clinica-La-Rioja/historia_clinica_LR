package ar.lamansys.online.infraestructure.output.repository.institution;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import ar.lamansys.online.infraestructure.output.entity.BookingInstitution;
import ar.lamansys.online.infraestructure.output.repository.BookingInstitutionRepository;
import net.pladema.sgx.backoffice.repository.BackofficeStore;

@Service
public class BackofficeBookingInstitutionStore implements BackofficeStore<BackofficeBookingInstitutionDto, Integer> {
	private final BookingInstitutionRepository repository;

	public BackofficeBookingInstitutionStore(BookingInstitutionRepository repository) {
		this.repository = repository;
	}

	@Override
	public Page<BackofficeBookingInstitutionDto> findAll(BackofficeBookingInstitutionDto dto, Pageable pageable) {
		return repository.findAll(Example.of(new BookingInstitution(dto.getId())), pageable)
				.map(bookingInstitution -> new BackofficeBookingInstitutionDto(bookingInstitution.getInstitutionId()));
	}

	@Override
	public List<BackofficeBookingInstitutionDto> findAll() {
		return repository.findAll().stream()
				.map(bookingInstitution -> new BackofficeBookingInstitutionDto(bookingInstitution.getInstitutionId()))
				.collect(Collectors.toList());
	}

	@Override
	public List<BackofficeBookingInstitutionDto> findAllById(List<Integer> ids) {
		return repository.findAllById(ids).stream()
				.map(bookingInstitution -> new BackofficeBookingInstitutionDto(bookingInstitution.getInstitutionId()))
				.collect(Collectors.toList());
	}

	@Override
	public Optional<BackofficeBookingInstitutionDto> findById(Integer id) {
		return repository.findById(id)
				.map(bookingInstitution -> new BackofficeBookingInstitutionDto(bookingInstitution.getInstitutionId()));
	}

	@Override
	public BackofficeBookingInstitutionDto save(BackofficeBookingInstitutionDto dto) {
		return new BackofficeBookingInstitutionDto(
				repository.findById(dto.getId())
				.orElse(repository.save(new BookingInstitution(dto.getId())))
				.getInstitutionId());
	}
	
	@Override
	public void deleteById(Integer id) {
		repository.deleteById(id);
	}

	@Override
	public Example<BackofficeBookingInstitutionDto> buildExample(BackofficeBookingInstitutionDto entity) {
		return Example.of(entity);
	}

}
