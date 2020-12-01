package net.pladema.clinichistory.documents.core.ips;

import net.pladema.clinichistory.documents.service.DocumentService;
import net.pladema.clinichistory.documents.repository.ips.ProceduresRepository;
import net.pladema.clinichistory.documents.repository.ips.entity.Procedure;
import net.pladema.clinichistory.documents.repository.ips.masterdata.ProceduresStatusRepository;
import net.pladema.clinichistory.documents.repository.ips.masterdata.entity.ProceduresStatus;
import net.pladema.clinichistory.documents.service.ips.ProceduresService;
import net.pladema.clinichistory.documents.service.ips.SnomedService;
import net.pladema.clinichistory.outpatient.createoutpatient.service.domain.ProcedureBo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProceduresServiceImpl implements ProceduresService {

    private static final Logger LOG = LoggerFactory.getLogger(ProceduresServiceImpl.class);

    public static final String OUTPUT = "Output -> {}";

    private final ProceduresRepository proceduresRepository;

    private final ProceduresStatusRepository proceduresStatusRepository;

    private final DocumentService documentService;

    private final SnomedService snomedService;


    public ProceduresServiceImpl(ProceduresRepository proceduresRepository,
                                 ProceduresStatusRepository proceduresStatusRepository,
                                 DocumentService documentService,
                                 SnomedService snomedService){
        this.proceduresRepository = proceduresRepository;
        this.proceduresStatusRepository = proceduresStatusRepository;
        this.documentService = documentService;
        this.snomedService = snomedService;
    }

    @Override
    public List<ProcedureBo> loadProcedures(Integer patientId, Long documentId, List<ProcedureBo> procedures) {
        LOG.debug("Input parameters -> patientId {}, documentId {}, procedures {}", documentId, patientId, procedures);
        procedures.forEach(p -> {
            String sctId = snomedService.createSnomedTerm(p.getSnomed());
            Procedure procedure = saveProcedure(patientId, p, sctId);

            p.setId(procedure.getId());
            p.setStatusId(procedure.getStatusId());
            p.setStatus(getStatus(p.getStatusId()));

            documentService.createDocumentProcedure(documentId, procedure.getId());
        });
        List<ProcedureBo> result = procedures;
        LOG.debug(OUTPUT, result);
        return result;
    }

    private Procedure saveProcedure(Integer patientId, ProcedureBo procedureBo, String sctId) {
        LOG.debug("Input parameters -> patientId {}, procedureBo {}, sctId {}", patientId, procedureBo, sctId);
        Procedure result = new Procedure(
                patientId,
                sctId,
                procedureBo.getStatusId(), procedureBo.getPerformedDate());

        result = proceduresRepository.save(result);
        LOG.debug("Procedure saved -> {}", result.getId());
        LOG.debug(OUTPUT, result);
        return result;
    }

    private String getStatus(String id) {
        return proceduresStatusRepository.findById(id).map(ProceduresStatus::getDescription).orElse(null);
    }

}
