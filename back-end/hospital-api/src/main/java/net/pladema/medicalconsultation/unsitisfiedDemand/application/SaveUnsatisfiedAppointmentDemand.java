package net.pladema.medicalconsultation.unsitisfiedDemand.application;

import ar.lamansys.sgx.shared.dates.repository.entity.EDayOfWeek;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.pladema.medicalconsultation.unsitisfiedDemand.application.exception.UnsatisfiedAppointmentDemandException;
import net.pladema.medicalconsultation.unsitisfiedDemand.domain.UnsatisfiedAppointmentDemandBo;

import net.pladema.medicalconsultation.unsitisfiedDemand.domain.exception.EUnsatisfiedAppointmentDemandCode;
import net.pladema.medicalconsultation.unsitisfiedDemand.infrastructure.output.entity.UnsatisfiedAppointmentDemand;

import net.pladema.medicalconsultation.unsitisfiedDemand.infrastructure.output.entity.UnsatisfiedAppointmentDemandDay;
import net.pladema.medicalconsultation.unsitisfiedDemand.infrastructure.output.entity.embedded.UnsatisfiedAppointmentDemandDayPK;
import net.pladema.medicalconsultation.unsitisfiedDemand.infrastructure.output.repository.UnsatisfiedAppointmentDemandDayRepository;
import net.pladema.medicalconsultation.unsitisfiedDemand.infrastructure.output.repository.UnsatisfiedAppointmentDemandRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
@Service
public class SaveUnsatisfiedAppointmentDemand {

	private UnsatisfiedAppointmentDemandRepository unsatisfiedAppointmentDemandRepository;

	private UnsatisfiedAppointmentDemandDayRepository unsatisfiedAppointmentDemandDayRepository;

	@Transactional
	public Integer run(UnsatisfiedAppointmentDemandBo unsatisfiedAppointmentDemand) {
		log.debug("Input parameters -> unsatisfiedAppointmentDemand {}", unsatisfiedAppointmentDemand);
		assertValidUnsatisfiedAppointmentDemand(unsatisfiedAppointmentDemand);
		UnsatisfiedAppointmentDemand entity = parseEntity(unsatisfiedAppointmentDemand);
		Integer result = unsatisfiedAppointmentDemandRepository.save(entity).getId();
		saveUnsatisfiedAppointmentDemandDays(result, unsatisfiedAppointmentDemand.getDaysOfWeek());
		log.debug("Output -> {}", result);
		return result;
	}

	private void assertValidUnsatisfiedAppointmentDemand(UnsatisfiedAppointmentDemandBo unsatisfiedAppointmentDemand) {
		if (unsatisfiedAppointmentDemand.getInitialSearchDate() == null)
			throw new UnsatisfiedAppointmentDemandException(EUnsatisfiedAppointmentDemandCode.WRONG_PARAMETER, "La fecha inicial de búsqueda es obligatoria para el registro de la demanda");
		if (unsatisfiedAppointmentDemand.getEndingSearchDate() == null)
			throw new UnsatisfiedAppointmentDemandException(EUnsatisfiedAppointmentDemandCode.WRONG_PARAMETER, "La fecha final de búsqueda es obligatoria para el registro de la demanda");
		if (unsatisfiedAppointmentDemand.getInitialSearchTime() == null)
			throw new UnsatisfiedAppointmentDemandException(EUnsatisfiedAppointmentDemandCode.WRONG_PARAMETER, "La hora inicial de búsqueda es obligatoria para el registro de la demanda");
		if (unsatisfiedAppointmentDemand.getEndSearchTime() == null)
			throw new UnsatisfiedAppointmentDemandException(EUnsatisfiedAppointmentDemandCode.WRONG_PARAMETER, "La hora final de búsqueda es obligatoria para el registro de la demanda");
		if (unsatisfiedAppointmentDemand.getModalityId() == null)
			throw new UnsatisfiedAppointmentDemandException(EUnsatisfiedAppointmentDemandCode.WRONG_PARAMETER, "La modalidad de búsqueda es obligatoria para el registro de la demanda");
		if (unsatisfiedAppointmentDemand.getPracticeId() == null && unsatisfiedAppointmentDemand.getAliasOrSpecialtyName() == null)
			throw new UnsatisfiedAppointmentDemandException(EUnsatisfiedAppointmentDemandCode.WRONG_PARAMETER, "La práctica o especialidad de búsqueda son necesarias para el registro de la demanda");
		if (unsatisfiedAppointmentDemand.getDaysOfWeek() == null || unsatisfiedAppointmentDemand.getDaysOfWeek().isEmpty())
			throw new UnsatisfiedAppointmentDemandException(EUnsatisfiedAppointmentDemandCode.WRONG_PARAMETER, "Los días de la semana de búsqueda son necesarios para el registro de la demanda");
		assertValidDaysOfWeek(unsatisfiedAppointmentDemand);
	}

	private void assertValidDaysOfWeek(UnsatisfiedAppointmentDemandBo unsatisfiedAppointmentDemand) {
		Set<Short> notRepeatedDates = new HashSet<>(unsatisfiedAppointmentDemand.getDaysOfWeek());
		if (notRepeatedDates.size() < unsatisfiedAppointmentDemand.getDaysOfWeek().size())
			throw new UnsatisfiedAppointmentDemandException(EUnsatisfiedAppointmentDemandCode.WRONG_PARAMETER, "Existen días de la semana repetidos dentro de la búsqueda");
		if (!validDates(notRepeatedDates))
			throw new UnsatisfiedAppointmentDemandException(EUnsatisfiedAppointmentDemandCode.WRONG_PARAMETER, "Se han detectado días de la semana que no existen");
	}

	private boolean validDates(Set<Short> notRepeatedDates) {
		Short minValue = notRepeatedDates.stream().min(Short::compare).get();
		Short maxValue = notRepeatedDates.stream().max(Short::compare).get();
		final short WEEK_DAY_AMOUNT = 7;
		return notRepeatedDates.size() <= WEEK_DAY_AMOUNT && minValue >= EDayOfWeek.SUNDAY.getId() && maxValue <= EDayOfWeek.SATURDAY.getId();
	}

	private void saveUnsatisfiedAppointmentDemandDays(Integer unsatisfiedDemandId, List<Short> daysOfWeek) {
		List<UnsatisfiedAppointmentDemandDay> days = daysOfWeek.stream().map(dayId -> parseUnsatisfiedAppointmentDemandDayEntity(unsatisfiedDemandId, dayId)).collect(Collectors.toList());
		unsatisfiedAppointmentDemandDayRepository.saveAll(days);
	}

	private UnsatisfiedAppointmentDemandDay parseUnsatisfiedAppointmentDemandDayEntity(Integer unsatisfiedDemandId, Short dayId) {
		UnsatisfiedAppointmentDemandDayPK pk = new UnsatisfiedAppointmentDemandDayPK(unsatisfiedDemandId, dayId);
		return new UnsatisfiedAppointmentDemandDay(pk);
	}

	private UnsatisfiedAppointmentDemand parseEntity(UnsatisfiedAppointmentDemandBo unsatisfiedAppointmentDemand) {
		UnsatisfiedAppointmentDemand result = new UnsatisfiedAppointmentDemand();
		result.setClinicalSpecialtyNameOrAlias(unsatisfiedAppointmentDemand.getAliasOrSpecialtyName());
		result.setInitialSearchTime(unsatisfiedAppointmentDemand.getInitialSearchTime());
		result.setEndSearchTime(unsatisfiedAppointmentDemand.getEndSearchTime());
		result.setInitialSearchDate(unsatisfiedAppointmentDemand.getInitialSearchDate());
		result.setEndSearchDate(unsatisfiedAppointmentDemand.getEndingSearchDate());
		result.setModalityId(unsatisfiedAppointmentDemand.getModalityId());
		result.setPracticeId(unsatisfiedAppointmentDemand.getPracticeId());
		result.setInstitutionId(unsatisfiedAppointmentDemand.getInstitutionId());
		return result;
	}

}
