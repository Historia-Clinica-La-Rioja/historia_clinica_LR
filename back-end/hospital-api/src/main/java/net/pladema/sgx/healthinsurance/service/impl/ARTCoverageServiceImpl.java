package net.pladema.sgx.healthinsurance.service.impl;

import java.util.Collection;
import java.util.stream.Collectors;

import net.pladema.patient.controller.dto.EMedicalCoverageType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import net.pladema.patient.repository.MedicalCoverageRepository;
import net.pladema.patient.repository.entity.MedicalCoverage;
import net.pladema.patient.service.domain.ARTCoverageBo;
import net.pladema.sgx.healthinsurance.service.ARTCoverageService;

@RequiredArgsConstructor
@Service
public class ARTCoverageServiceImpl implements ARTCoverageService {

	private static final Logger LOG = LoggerFactory.getLogger(ARTCoverageServiceImpl.class);

	private final MedicalCoverageRepository medicalCoverageRepository;

	@Override
	public Collection<ARTCoverageBo> getAll() {
		Collection<MedicalCoverage> medicalCoveragedata = medicalCoverageRepository.findAllByType(Sort.by(Sort.Direction.ASC, "name"), EMedicalCoverageType.ART.getId());
		Collection<ARTCoverageBo> result = medicalCoveragedata.stream().map(mc -> new ARTCoverageBo(mc.getId(), mc.getName(), mc.getCuit(), mc.getType())).collect(Collectors.toList());
		LOG.debug("Output -> {}", result);
		return result;
	}
}
