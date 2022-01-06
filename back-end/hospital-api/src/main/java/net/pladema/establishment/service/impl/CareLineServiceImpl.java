package net.pladema.establishment.service.impl;

import net.pladema.establishment.repository.CareLineRepository;
import net.pladema.establishment.service.CareLineService;
import net.pladema.establishment.service.domain.CareLineBo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CareLineServiceImpl implements CareLineService {

    private static final Logger LOG = LoggerFactory.getLogger(CareLineServiceImpl.class);

    private static final String OUTPUT = "Output -> {}";

    private final CareLineRepository careLineRepository;

    public CareLineServiceImpl(CareLineRepository careLineRepository) {
        this.careLineRepository = careLineRepository;
    }

    @Override
    public List<CareLineBo> getCareLines() {
        LOG.debug("No input parameters");
        List<CareLineBo> careLines = careLineRepository.getCareLinesWhitClinicalSpecialties()
                .stream()
                .map(careLine -> new CareLineBo(careLine.getId(), careLine.getDescription()))
                .collect(Collectors.toList());
        LOG.trace(OUTPUT, careLines);
        return careLines;
    }

}
