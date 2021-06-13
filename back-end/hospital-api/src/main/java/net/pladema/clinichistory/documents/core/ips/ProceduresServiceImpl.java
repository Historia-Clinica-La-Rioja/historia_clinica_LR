package net.pladema.clinichistory.documents.core.ips;

import net.pladema.clinichistory.documents.core.cie10.CalculateCie10Facade;
import net.pladema.clinichistory.documents.core.cie10.Cie10FacadeRuleFeature;
import net.pladema.clinichistory.documents.repository.ips.ProceduresRepository;
import net.pladema.clinichistory.documents.repository.ips.entity.Procedure;
import net.pladema.clinichistory.documents.repository.ips.masterdata.ProceduresStatusRepository;
import net.pladema.clinichistory.documents.repository.ips.masterdata.entity.ProceduresStatus;
import net.pladema.clinichistory.documents.service.DocumentService;
import net.pladema.clinichistory.documents.service.domain.PatientInfoBo;
import net.pladema.clinichistory.documents.service.ips.ProceduresService;
import net.pladema.clinichistory.documents.service.ips.SnomedService;
import net.pladema.clinichistory.outpatient.createoutpatient.service.domain.ProcedureBo;
import net.pladema.snowstorm.services.domain.Cie10RuleFeature;
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

    private final CalculateCie10Facade calculateCie10Facade;

    public ProceduresServiceImpl(ProceduresRepository proceduresRepository,
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

    @Override
    public List<ProcedureBo> loadProcedures(PatientInfoBo patientInfo, Long documentId, List<ProcedureBo> procedures) {
        LOG.debug("Input parameters -> patientInfo {}, documentId {}, procedures {}", patientInfo, documentId, procedures);
        procedures.forEach(p -> {
            Integer snomedId = snomedService.getSnomedId(p.getSnomed())
                    .orElseGet(() -> snomedService.createSnomedTerm(p.getSnomed()));
            String cie10Codes = calculateCie10Facade.execute(p.getSnomed().getSctid(),
                    new Cie10FacadeRuleFeature(patientInfo.getGenderId(), patientInfo.getAge()));
            Procedure procedure = saveProcedure(patientInfo.getId(), p, snomedId, cie10Codes);

            p.setId(procedure.getId());
            p.setStatusId(procedure.getStatusId());
            p.setStatus(getStatus(p.getStatusId()));

            documentService.createDocumentProcedure(documentId, procedure.getId());
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
