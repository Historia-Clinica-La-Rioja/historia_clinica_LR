package ar.lamansys.online.application.specialty;

import ar.lamansys.online.domain.specialty.BookingSpecialtyBo;

import lombok.AllArgsConstructor;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@AllArgsConstructor
@Service
public class FetchSpecialtiesByProfessionals {

	private final BookingSpecialtyStorage practiceStorage;

	public List<BookingSpecialtyBo> run() {
		var result = practiceStorage.findSpecialtiesByProfessionals();
		log.debug("specialties => {}", result);
		return result;
	}
}
