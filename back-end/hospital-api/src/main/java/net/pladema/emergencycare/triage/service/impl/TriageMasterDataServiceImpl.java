package net.pladema.emergencycare.triage.service.impl;

import net.pladema.emergencycare.triage.repository.TriageCategoryRepository;
import net.pladema.emergencycare.triage.service.TriageMasterDataService;
import net.pladema.emergencycare.triage.service.domain.TriageCategoryBo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TriageMasterDataServiceImpl implements TriageMasterDataService {

    private static final Logger LOG = LoggerFactory.getLogger(TriageMasterDataServiceImpl.class);

    private final TriageCategoryRepository triageCategoryRepository;

    public TriageMasterDataServiceImpl(TriageCategoryRepository triageCategoryRepository) {
        this.triageCategoryRepository = triageCategoryRepository;
    }

    @Override
    public List<TriageCategoryBo> getCategories() {
        LOG.debug("No input parameters");
        List<TriageCategoryBo> categories = triageCategoryRepository.findAll().stream()
                .map(TriageCategoryBo::new)
                .collect(Collectors.toList());
        LOG.debug("Output size = {}", categories.size());
        LOG.trace("Output -> {}", categories);
        return categories;
    }
}
