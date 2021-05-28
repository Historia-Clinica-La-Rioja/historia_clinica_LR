package net.pladema.clinichistory.documents.core.ips;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.ImmunizationRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.Inmunization;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.ImmunizationStatusRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.entity.InmunizationStatus;
import net.pladema.clinichistory.documents.core.cie10.CalculateCie10Facade;
import net.pladema.clinichistory.documents.core.cie10.Cie10FacadeRuleFeature;
import net.pladema.clinichistory.documents.service.DocumentService;
import net.pladema.clinichistory.documents.service.NoteService;
import net.pladema.clinichistory.documents.service.domain.PatientInfoBo;
import net.pladema.clinichistory.documents.service.ips.ImmunizationService;
import net.pladema.clinichistory.documents.service.ips.SnomedService;
import ar.lamansys.sgh.clinichistory.domain.ips.ImmunizationBo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class ImmunizationServiceImpl implements ImmunizationService {

    private static final Logger LOG = LoggerFactory.getLogger(ImmunizationServiceImpl.class);

    public static final String OUTPUT = "Output -> {}";

    private final ImmunizationRepository immunizationRepository;

    private final ImmunizationStatusRepository immunizationStatusRepository;

    private final SnomedService snomedService;

    private final CalculateCie10Facade calculateCie10Facade;

    private final DocumentService documentService;

    private final NoteService noteService;

    public ImmunizationServiceImpl(ImmunizationRepository immunizationRepository,
                                   ImmunizationStatusRepository immunizationStatusRepository,
                                   SnomedService snomedService,
                                   CalculateCie10Facade calculateCie10Facade,
                                   DocumentService documentService,
                                   NoteService noteService){
        this.immunizationRepository = immunizationRepository;
        this.immunizationStatusRepository = immunizationStatusRepository;
        this.snomedService = snomedService;
        this.calculateCie10Facade = calculateCie10Facade;
        this.documentService = documentService;
        this.noteService = noteService;
    }

    @Override
    public List<ImmunizationBo> loadImmunization(PatientInfoBo patientInfo, Long documentId, List<ImmunizationBo> immunizations) {
        LOG.debug("Input parameters -> patientInfo {}, documentId {}, immunizations {}", patientInfo, documentId, immunizations);
        immunizations.forEach(i -> {
            Integer snomedId = snomedService.getSnomedId(i.getSnomed())
                    .orElseGet(() -> snomedService.createSnomedTerm(i.getSnomed()));
            String cie10Codes = calculateCie10Facade.execute(i.getSnomed().getSctid(),
                    new Cie10FacadeRuleFeature(patientInfo.getGenderId(), patientInfo.getAge()));

            AtomicReference<Long> noteId = new AtomicReference<>(null);
            Optional.ofNullable(i.getNote()).ifPresent(n ->
                noteId.set(loadNote(n))
            );

            Inmunization immunization = saveImmunization(patientInfo.getId(), i, snomedId, cie10Codes, noteId.get());

            i.setId(immunization.getId());
            i.setStatusId(immunization.getStatusId());
            i.setStatus(getStatus(i.getStatusId()));
            i.setAdministrationDate(immunization.getAdministrationDate());

            documentService.createImmunization(documentId, immunization.getId());
        });
        List<ImmunizationBo> result = immunizations;
        LOG.debug(OUTPUT, result);
        return result;
    }

    private Inmunization saveImmunization(Integer patientId, ImmunizationBo immunizationBo, Integer snomedId, String cie10Codes, Long noteId) {
        LOG.debug("Input parameters -> patientId {}, immunizationBo {}, snomedId {}, noteId {}", patientId, immunizationBo, snomedId, noteId);
        Inmunization immunization = new Inmunization(patientId, snomedId, cie10Codes, immunizationBo.getStatusId()
                , immunizationBo.getAdministrationDate(), immunizationBo.getInstitutionId(), noteId);
        immunization = immunizationRepository.save(immunization);
        LOG.debug("Immunization saved -> {}", immunization.getId());
        LOG.debug(OUTPUT, immunization);
        return immunization;
    }

    private String getStatus(String id) {
        return immunizationStatusRepository.findById(id).map(InmunizationStatus::getDescription).orElse(null);
    }


    private Long loadNote(String note) {
        LOG.debug("Input parameters -> note {}", note);
        Long result = noteService.createNote(note);
        LOG.debug(OUTPUT, result);
        return result;
    }

}
