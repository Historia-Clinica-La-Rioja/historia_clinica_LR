package net.pladema.emergencycare.triage.service.impl;

import net.pladema.emergencycare.triage.repository.TriageCategoryRepository;

import net.pladema.emergencycare.triage.repository.entity.TriageCategory;
import net.pladema.emergencycare.triage.service.TriageMasterDataService;
import net.pladema.emergencycare.triage.service.domain.TriageCategoryBo;

import net.pladema.emergencycare.triage.service.domain.enums.EBodyTemperature;
import net.pladema.emergencycare.triage.service.domain.enums.EMuscleHypertonia;
import net.pladema.emergencycare.triage.service.domain.enums.EPerfusion;
import net.pladema.emergencycare.triage.service.domain.enums.ERespiratoryRetraction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

import java.util.stream.Collectors;

@Service
public class TriageMasterDataServiceImpl implements TriageMasterDataService {

    private static final Logger LOG = LoggerFactory.getLogger(TriageMasterDataServiceImpl.class);

    public static final String OUTPUT = "Output -> {}";

    private final TriageCategoryRepository triageCategoryRepository;

    public TriageMasterDataServiceImpl(TriageCategoryRepository triageCategoryRepository){
        super();
        this.triageCategoryRepository=triageCategoryRepository;
    }

    @Override
    public List<TriageCategoryBo> getCategories() {
        LOG.debug("No input parameters");
        List<TriageCategoryBo> categories = triageCategoryRepository.findAll().stream()
                .map(TriageCategoryBo::new)
                .collect(Collectors.toList());
        LOG.debug("Output size = {}", categories.size());
        LOG.trace(OUTPUT, categories);
        return categories;
    }

    @Override
    public TriageCategoryBo getCategoryById(Short categoryId) {
        LOG.debug("Input parameter -> categoryId {}", categoryId);
        return triageCategoryRepository.findById(categoryId)
				.map(tc -> {
					TriageCategoryBo result = new TriageCategoryBo(tc);
					LOG.debug(OUTPUT, result);
					return result;
				}).get();
    }

    @Override
    public List<EBodyTemperature> getBodyTemperature() {
        List<EBodyTemperature> result = EBodyTemperature.getAll();
        LOG.debug(OUTPUT, result);
        return result;
    }

    @Override
    public List<EMuscleHypertonia> getMuscleHypertonia() {
        List<EMuscleHypertonia> result = EMuscleHypertonia.getAll();
        LOG.debug(OUTPUT, result);
        return result;
    }

    @Override
    public List<ERespiratoryRetraction> getRespiratoryRetraction() {
        List<ERespiratoryRetraction> result = ERespiratoryRetraction.getAll();
        LOG.debug(OUTPUT, result);
        return result;
    }

    @Override
    public List<EPerfusion> getPerfusion() {
        List<EPerfusion> result = EPerfusion.getAll();
        LOG.debug(OUTPUT, result);
        return result;
    }
}
