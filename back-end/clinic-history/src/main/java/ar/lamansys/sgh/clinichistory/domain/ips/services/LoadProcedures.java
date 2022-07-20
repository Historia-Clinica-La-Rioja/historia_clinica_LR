package ar.lamansys.sgh.clinichistory.domain.ips.services;

import ar.lamansys.sgh.clinichistory.application.calculatecie10.CalculateCie10Facade;
import ar.lamansys.sgh.clinichistory.application.calculatecie10.Cie10FacadeRuleFeature;
import ar.lamansys.sgh.clinichistory.application.document.DocumentService;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.ProceduresRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.Procedure;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.ProceduresStatusRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.entity.ProceduresStatus;
import ar.lamansys.sgh.clinichistory.domain.document.PatientInfoBo;
import ar.lamansys.sgh.clinichistory.domain.ips.ProcedureBo;
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

    private final CalculateCie10Facade calculateCie10Facade;

    public LoadProcedures(ProceduresRepository proceduresRepository,
                          ProceduresStatusRepository proceduresStatusRepository,
                          DocumentService documentService,
                          SnomedService snomedService,
                          CalculateCie10Facade calculateCie10Facade){
        this.proceduresRepository = proceduresRepository;
        this.proceduresStatusRepository = proceduresStatusRepository;
        this.documentService = documentService;
        this.snomedService = snomedService;
        this.calculateCie10Facade = calculateCie10Facade;
    }

    public List<ProcedureBo> run(PatientInfoBo patientInfo, Long documentId, List<ProcedureBo> procedures) {
        LOG.debug("Input parameters -> patientInfo {}, documentId {}, procedures {}", patientInfo, documentId, procedures);
        procedures.forEach(p -> {
			if(p.getId()==null) {
				Integer snomedId = snomedService.getSnomedId(p.getSnomed()).orElseGet(() -> snomedService.createSnomedTerm(p.getSnomed()));
				String cie10Codes = calculateCie10Facade.execute(p.getSnomed().getSctid(), new Cie10FacadeRuleFeature(patientInfo.getGenderId(), patientInfo.getAge()));
				Procedure procedure = saveProcedure(patientInfo.getId(), p, snomedId, cie10Codes);

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

    private Procedure saveProcedure(Integer patientId, ProcedureBo procedureBo, Integer snomedId, String cie10Codes) {
        LOG.debug("Input parameters -> patientId {}, procedureBo {}, snomedId {}", patientId, procedureBo, snomedId);
        Procedure result = new Procedure(
                patientId,
                snomedId,
                cie10Codes,
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
