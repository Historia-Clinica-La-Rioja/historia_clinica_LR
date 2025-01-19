package ar.lamansys.sgh.clinichistory.domain.ips.services;

import ar.lamansys.sgh.clinichistory.domain.ips.ProcedureBo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class LoadProcedures {

    public static final String OUTPUT = "Output -> {}";
    
    private final LoadProcedure loadProcedure;
    
    public List<ProcedureBo> run(Integer patientId, Long documentId, List<ProcedureBo> procedures) {
        log.debug("Input parameters -> patientId {}, documentId {}, procedures {}", patientId, documentId, procedures);
        procedures.forEach(procedure -> loadProcedure.run(patientId, documentId, procedure));
        log.debug(OUTPUT, procedures);
        return procedures;
    }

}
