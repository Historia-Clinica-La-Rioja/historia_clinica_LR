package net.pladema.establishment.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import net.pladema.address.controller.service.domain.AddressBo;
import net.pladema.address.service.AddressService;
import net.pladema.establishment.repository.InstitutionRepository;
import net.pladema.establishment.service.InstitutionBoMapper;
import net.pladema.establishment.service.InstitutionService;
import net.pladema.establishment.service.domain.InstitutionBasicInfoBo;
import net.pladema.establishment.service.domain.InstitutionBo;

@Service
@Slf4j
public class InstitutionServiceImpl implements InstitutionService {

	private static final Logger LOG = LoggerFactory.getLogger(InstitutionServiceImpl.class);

	private static final String LOGGING_INPUT = "Input parameters -> institutionId {} ";

	private final InstitutionRepository institutionRepository;

	private final InstitutionBoMapper institutionBoMapper;

	private final AddressService addressService;

	public InstitutionServiceImpl(InstitutionRepository institutionRepository, InstitutionBoMapper institutionBoMapper, AddressService addressService) {
		this.institutionRepository = institutionRepository;
		this.institutionBoMapper = institutionBoMapper;
		this.addressService = addressService;
	}

	@Override
	public List<InstitutionBasicInfoBo> getInstitutionsByImageSectors(){
		return institutionRepository.getByDiagnosisImagesSectors();
	}

	@Override
	public InstitutionBo get(Integer id) {
		LOG.debug(LOGGING_INPUT, id);
		return institutionRepository.findById(id)
				.map(institutionBoMapper::toInstitutionBo)
				.orElse(null);
	}

	@Override
	public InstitutionBo get(String sisaCode) {
		LOG.debug(LOGGING_INPUT, sisaCode);
		return institutionRepository.findBySisaCode(sisaCode)
				.map(institutionBoMapper::toInstitutionBo)
				.orElse(null);
	}

	@Override
	public AddressBo getAddress(Integer institutionId) {
		return addressService.getAddressByInstitution(institutionId);
	}

	@Override
	public ar.lamansys.sgh.shared.domain.general.AddressBo getInstitutionAddress(Integer institutionId) {
		return addressService.getAddressDataByInstitution(institutionId);
	}

	@Override
	public List<InstitutionBasicInfoBo> getInstitutionsByReferenceByClinicalSpecialtyFilter(Short departmentId, List<Integer> clinicalSpecialtyIds, Integer careLineId) {
		log.debug("Fetch all institutions by reference by clinical specialty filter and active diaries");
		if (careLineId == null )
			return institutionRepository.getByDepartmentIdHavingActiveDiaryWithClinicalSpecialty(departmentId, clinicalSpecialtyIds);
		else
			return institutionRepository.getByDepartmentIdHavingActiveDiaryWithCareLineClinicalSpecialty(departmentId, careLineId, clinicalSpecialtyIds);
	}

	@Override
	public List<InstitutionBasicInfoBo> getVirtualConsultationInstitutions() {
		List<InstitutionBasicInfoBo> result = institutionRepository.getVirtualConsultationInstitutions();
		log.debug("Output -> {}", result);
		return result;
	}

	@Override
	public List<InstitutionBasicInfoBo> getInstitutionsByReferenceByPracticeFilter(Short departmentId, Integer practiceSnomedId,
																				   List<Integer> clinicalSpecialtyIds, Integer careLineId) {
		log.debug("Input parameters -> departmentId {}, practiceSnomedId {}, clinicalSpecialtyIds {}, careLineId {}", departmentId, practiceSnomedId, clinicalSpecialtyIds, careLineId);
		if (careLineId != null && clinicalSpecialtyIds != null && !clinicalSpecialtyIds.isEmpty())
			return institutionRepository.getByDepartmentAndCareLineAndPracticeAndClinicalSpecialty(departmentId, clinicalSpecialtyIds, careLineId, practiceSnomedId);
		if (careLineId != null)
			return institutionRepository.getByDepartmentAndCareLineAndPractice(departmentId, careLineId, practiceSnomedId);
		if (clinicalSpecialtyIds != null && !clinicalSpecialtyIds.isEmpty())
			return institutionRepository.getAllByDepartmentAndClinicalSpecialtyAndPractice(departmentId, clinicalSpecialtyIds, practiceSnomedId);
		return institutionRepository.getByDepartmentAndPractice(departmentId, practiceSnomedId);
	}
}
