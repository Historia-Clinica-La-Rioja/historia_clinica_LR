package net.pladema.clinichistory.hospitalization.application.isadministrativedischarged;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.clinichistory.hospitalization.repository.PatientDischargeRepository;
import net.pladema.clinichistory.hospitalization.service.domain.PatientDischargeBo;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class IsAdministrativeDischarged {

    private final PatientDischargeRepository patientDischargeRepository;

    public boolean run(Integer internmentEpisodeId) {
        log.debug("Input parameters -> internmentEpisodeId {}", internmentEpisodeId);
        Optional<LocalDateTime> administrativeDischargeDate = patientDischargeRepository.findById(internmentEpisodeId)
                .map(PatientDischargeBo::new)
                .map(PatientDischargeBo::getAdministrativeDischargeDate);
        var result = administrativeDischargeDate.isPresent();
        log.debug("Output -> {}", result);
        return result;
    }
}
