package net.pladema.clinichistory.hospitalization.infrastructure.output;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.anestheticreport.AnestheticReport;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.anestheticreport.AnestheticReportRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.clinichistory.hospitalization.application.port.AnestheticStorage;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class AnestheticStorageImpl implements AnestheticStorage {

    private final AnestheticReportRepository anestheticReportRepository;

    public Integer save() {
        log.trace("Input parameters -> ");

        Integer result = anestheticReportRepository.save(new AnestheticReport()).getId();

        log.trace("Output -> {}", result);
        return result;
    }

}
