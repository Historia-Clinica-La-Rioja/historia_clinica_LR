package ar.lamansys.sgh.clinichistory.domain.ips.services;

import ar.lamansys.sgh.clinichistory.domain.ips.MedicationBo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class LoadMedications {

    public static final String OUTPUT = "Output -> {}";

    private final LoadMedication loadMedication;

    public List<MedicationBo> run(Integer patientId, Long documentId, List<MedicationBo> medications) {
        log.debug("Input parameters -> patientId {}, documentId {}, medications {}", patientId, documentId, medications);
        medications.forEach(medication -> loadMedication.run(patientId, documentId, medication));
        log.debug(OUTPUT, medications);
        return medications;
    }

}
