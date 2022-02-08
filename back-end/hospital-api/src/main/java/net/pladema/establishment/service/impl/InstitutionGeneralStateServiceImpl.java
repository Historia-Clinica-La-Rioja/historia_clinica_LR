package net.pladema.establishment.service.impl;

import ar.lamansys.sgh.clinichistory.domain.ips.ERiskFactor;
import net.pladema.establishment.repository.VInstitutionRepository;
import net.pladema.establishment.repository.VInstitutionRiskFactorRepository;
import net.pladema.establishment.repository.entity.VInstitution;
import net.pladema.establishment.repository.entity.VInstitutionRiskFactor;
import net.pladema.establishment.service.InstitutionGeneralStateService;
import net.pladema.establishment.service.domain.VInstitutionBo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class InstitutionGeneralStateServiceImpl implements InstitutionGeneralStateService {

    private static final Logger LOG = LoggerFactory.getLogger(InstitutionGeneralStateServiceImpl.class);

    private final VInstitutionRepository vInstitutionRepository;

    private final VInstitutionRiskFactorRepository vInstitutionRiskFactorRepository;

    public InstitutionGeneralStateServiceImpl(VInstitutionRepository vInstitutionRepository,
                                              VInstitutionRiskFactorRepository vInstitutionRiskFactorRepository){
        this.vInstitutionRepository = vInstitutionRepository;
        this.vInstitutionRiskFactorRepository = vInstitutionRiskFactorRepository;
    }

    @Override
    public VInstitutionBo get(Integer institutionId) {
        LOG.debug("Input parameters -> institutionId {}", institutionId);

        List<VInstitution> generalState =  vInstitutionRepository.getGeneralState(institutionId);
        VInstitutionBo result = new VInstitutionBo();

        if(!generalState.isEmpty()){
            //#Georeferenciación
            VInstitution first = generalState.get(0);
            result.setLatitude(first.getLatitude());
            result.setLongitude(first.getLongitude());
        }

        //#Cantidad de adultos mayores alojados
        long count = generalState.stream()
                .map(i -> i.getPk().getInternmentEpisodeId())
                .distinct().count();
        result.setPatientCount(count);

        //#Cantidad de adultos mayores con diagnostico presuntivo de Covid
        count = generalState.stream()
                .filter(VInstitution::isCovidPresumtive)
                .count();
        result.setPatientWithCovidPresumtiveCount(count);

        List<VInstitutionRiskFactor> riskFactorGeneralState = vInstitutionRiskFactorRepository.getGeneralState(institutionId);
        List<VInstitutionRiskFactor> riskFactorsValid = riskFactorGeneralState.stream()
                .filter(i -> ERiskFactor.isCodeRiskFactor(i.getSctidCode()))
                .collect(Collectors.toList());

        //#Cantidad de adultos mayores con factores de riesgo cargados en las últimas 24hh
        count = riskFactorsValid.stream().filter(i -> i.getLoadDays() < 2).count();
        result.setPatientWithRiskFactorCount(count);

        //#Fecha más lejana de carga de factor de riesgo
        Optional<VInstitutionRiskFactor> minLoadRiskFactor = riskFactorsValid.stream()
                .min(Comparator.comparing( VInstitutionRiskFactor::getEffectiveTime ) );
        minLoadRiskFactor.ifPresent(v ->
            result.setLastDateRiskFactor(v.getEffectiveTime())
        );
        LOG.debug("Output -> {}", result);
        return result;
    }
}
