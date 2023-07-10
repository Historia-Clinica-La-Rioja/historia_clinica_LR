package ar.lamansys.sgh.clinichistory.domain.ips.services;

import ar.lamansys.sgh.clinichistory.application.document.DocumentService;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.ProceduresRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.Procedure;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.ProceduresStatusRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.entity.ProceduresStatus;
import ar.lamansys.sgh.clinichistory.domain.ips.ProcedureBo;
import ar.lamansys.sgh.shared.infrastructure.input.service.ProcedureTypeEnum;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoadProcedures {

    private static final Logger LOG = LoggerFactory.getLogger(LoadProcedures.class);

    public static final String OUTPUT = "Output -> {}";

    private final ProceduresRepository proceduresRepository;

    private final ProceduresStatusRepository proceduresStatusRepository;

    private final DocumentService documentService;

    private final SnomedService snomedService;

    public LoadProcedures(ProceduresRepository proceduresRepository,
                          ProceduresStatusRepository proceduresStatusRepository,
                          DocumentService documentService,
                          SnomedService snomedService){
        this.proceduresRepository = proceduresRepository;
        this.proceduresStatusRepository = proceduresStatusRepository;
        this.documentService = documentService;
        this.snomedService = snomedService;
    }

    public List<ProcedureBo> run(Integer patientId, Long documentId, List<ProcedureBo> procedures) {
        LOG.debug("Input parameters -> patientId {}, documentId {}, procedures {}", patientId, documentId, procedures);
        procedures.forEach(p -> {
			if(p.getId()==null) {
				Integer snomedId = snomedService.getSnomedId(p.getSnomed()).orElseGet(() -> snomedService.createSnomedTerm(p.getSnomed()));
				Procedure procedure = saveProcedure(patientId, p, snomedId);
				p.setId(procedure.getId());
				p.setStatusId(procedure.getStatusId());
				p.setStatus(getStatus(p.getStatusId()));
			}
            documentService.createDocumentProcedure(documentId, p.getId());
        });
        List<ProcedureBo> result = procedures;
        LOG.debug(OUTPUT, result);
        return result;
    }

    private Procedure saveProcedure(Integer patientId, ProcedureBo procedureBo, Integer snomedId) {
        LOG.debug("Input parameters -> patientId {}, procedureBo {}, snomedId {}", patientId, procedureBo, snomedId);
        Procedure result = new Procedure(
                patientId,
                snomedId,
                procedureBo.getStatusId(), procedureBo.getPerformedDate(),
				procedureBo.getType(),
                procedureBo.getIsPrimary());

        result = proceduresRepository.save(result);
        LOG.debug("Procedure saved -> {}", result.getId());
        LOG.debug(OUTPUT, result);
        return result;
    }

    private String getStatus(String id) {
        return proceduresStatusRepository.findById(id).map(ProceduresStatus::getDescription).orElse(null);
    }

}
