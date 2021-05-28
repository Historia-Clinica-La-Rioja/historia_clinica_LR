package net.pladema.establishment.service.impl;

import ar.lamansys.sgh.clinichistory.domain.ips.EVitalSign;
import net.pladema.establishment.repository.VInstitutionRepository;
import net.pladema.establishment.repository.VInstitutionVitalSignRepository;
import net.pladema.establishment.repository.entity.VInstitution;
import net.pladema.establishment.repository.entity.VInstitutionVitalSign;
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

    private final VInstitutionVitalSignRepository vInstitutionVitalSignRepository;

    public InstitutionGeneralStateServiceImpl(VInstitutionRepository vInstitutionRepository,
                                              VInstitutionVitalSignRepository vInstitutionVitalSignRepository){
        this.vInstitutionRepository = vInstitutionRepository;
        this.vInstitutionVitalSignRepository = vInstitutionVitalSignRepository;
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

        List<VInstitutionVitalSign> vitalSignGeneralState = vInstitutionVitalSignRepository.getGeneralState(institutionId);
        List<VInstitutionVitalSign> vitalSignsValid = vitalSignGeneralState.stream()
                .filter(i -> EVitalSign.isCodeVitalSign(i.getSctidCode()))
                .collect(Collectors.toList());

        //#Cantidad de adultos mayores con signos vitales cargados en las últimas 24hh
        count = vitalSignsValid.stream().filter(i -> i.getLoadDays() < 2).count();
        result.setPatientWithVitalSignCount(count);

        //#Fecha más lejana de carga de signo vital
        Optional<VInstitutionVitalSign> minLoadVitalSign = vitalSignsValid.stream()
                .min(Comparator.comparing( VInstitutionVitalSign::getEffectiveTime ) );
        minLoadVitalSign.ifPresent(v ->
            result.setLastDateVitalSign(v.getEffectiveTime())
        );
        LOG.debug("Output -> {}", result);
        return result;
    }
}
