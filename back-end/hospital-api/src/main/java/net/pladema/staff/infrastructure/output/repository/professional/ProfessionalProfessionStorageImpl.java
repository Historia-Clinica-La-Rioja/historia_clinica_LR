package net.pladema.staff.infrastructure.output.repository.professional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import net.pladema.staff.service.domain.ClinicalSpecialtyBo;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.staff.application.ports.ProfessionalProfessionStorage;
import net.pladema.staff.repository.HealthcareProfessionalSpecialtyRepository;
import net.pladema.staff.repository.ProfessionalProfessionRepository;
import net.pladema.staff.repository.domain.HealthcareProfessionalSpecialtyVo;
import net.pladema.staff.repository.domain.ProfessionalProfessionsVo;
import net.pladema.staff.repository.entity.HealthcareProfessionalSpecialty;
import net.pladema.staff.repository.entity.ProfessionalProfessions;
import net.pladema.staff.service.domain.HealthcareProfessionalSpecialtyBo;
import net.pladema.staff.service.domain.ProfessionalProfessionsBo;
import net.pladema.staff.service.domain.ProfessionalSpecialtyBo;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProfessionalProfessionStorageImpl implements ProfessionalProfessionStorage {

	private final ProfessionalProfessionRepository professionalProfessionRepository;

	private final HealthcareProfessionalSpecialtyRepository healthcareProfessionalSpecialtyRepository;

	@Override
	public List<ProfessionalProfessionsBo> getProfessionsByHealthcareProfessionalId(Integer healthcareProfessionalId) {
		log.debug("Input parameters -> healthcareProfessionalId {}", healthcareProfessionalId);
		List<ProfessionalProfessionsBo> result = professionalProfessionRepository.findByHealthcareProfessionalId(healthcareProfessionalId)
				.stream()
				.map(this::mapToProfessionalProfessionsBo)
				.peek(pbo -> pbo.setSpecialties(healthcareProfessionalSpecialtyRepository.getAllByProfessional(pbo.getId())
						.stream()
						.map(this::mapToHealthcareProfessionalSpecialtyBo)
						.collect(Collectors.toList()))).collect(Collectors.toList());
		log.debug("Output -> {}", result);
		return result;
	}

	@Override
	public void delete(Integer id) {
		log.debug("Input parameters -> professionalProfessionId {}", id);
		professionalProfessionRepository.deleteById(id);
		healthcareProfessionalSpecialtyRepository.deleteByProfessionalProfessionId(id);
	}

	@Override
	public void deleteHealthcareProfessionalSpecialty(Integer id) {
		log.debug("Input parameters -> healthcareProfessionalSpecialtyIf {}", id);
		healthcareProfessionalSpecialtyRepository.deleteById(id);
	}

	@Override
	@Transactional
	public Integer save(ProfessionalProfessionsBo bo) {
		log.debug("Input parameters -> professionalProfessionsBo {}", bo);
		ProfessionalProfessions professionalEntity = professionalProfessionRepository.findByProfessionalAndProfession(bo.getHealthcareProfessionalId(), bo.getProfession().getId()).map(pp -> {
			if(pp.isDeleted())
				pp.setDeleted(false);
			return pp;
		}).orElse(mapToEntity(bo));
		Integer result = professionalProfessionRepository.save(professionalEntity).getId();
		bo.getSpecialties().forEach(s -> {
			s.setProfessionalProfessionId(result);
			healthcareProfessionalSpecialtyRepository.findByProfessionAndSpecialty(result, s.getClinicalSpecialty().getId()).ifPresentOrElse(hps -> {
				if(hps.isDeleted())
				 hps.setDeleted(false);
				healthcareProfessionalSpecialtyRepository.save(hps);
			}, () -> healthcareProfessionalSpecialtyRepository.save(mapToHealthcareProfessionalSpecialty(s)));
		});
		log.debug("Output -> {}", result);
		return result;
	}

	private ProfessionalProfessions mapToEntity(ProfessionalProfessionsBo bo) {
		return new ProfessionalProfessions(bo.getId(), bo.getHealthcareProfessionalId(), bo.getProfession().getId());
	}

	private HealthcareProfessionalSpecialty mapToHealthcareProfessionalSpecialty(HealthcareProfessionalSpecialtyBo bo) {
		return new HealthcareProfessionalSpecialty(bo.getId(), bo.getProfessionalProfessionId(), bo.getClinicalSpecialty().getId());
	}

	private ProfessionalProfessionsBo mapToProfessionalProfessionsBo(ProfessionalProfessionsVo vo) {
		return new ProfessionalProfessionsBo(vo.getId(), vo.getHealthcareProfessionalId(), new ProfessionalSpecialtyBo(vo.getProfessionalSpecialtyId(), vo.getProfessionalSpecialtyName()), Collections.emptyList());
	}

	private HealthcareProfessionalSpecialtyBo mapToHealthcareProfessionalSpecialtyBo(HealthcareProfessionalSpecialtyVo vo) {
		return new HealthcareProfessionalSpecialtyBo(vo.getId(), vo.getHealthcareProfessionalId(), vo.getProfessionalProfessionId(), new ClinicalSpecialtyBo(vo.getClinicalSpecialtyId(),null), Boolean.FALSE);
	}

}
