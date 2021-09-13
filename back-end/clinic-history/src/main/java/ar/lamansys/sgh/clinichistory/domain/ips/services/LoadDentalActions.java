package ar.lamansys.sgh.clinichistory.domain.ips.services;

import ar.lamansys.sgh.clinichistory.application.calculatecie10.CalculateCie10Facade;
import ar.lamansys.sgh.clinichistory.application.calculatecie10.Cie10FacadeRuleFeature;
import ar.lamansys.sgh.clinichistory.application.document.DocumentService;
import ar.lamansys.sgh.clinichistory.domain.document.PatientInfoBo;
import ar.lamansys.sgh.clinichistory.domain.ips.DentalActionBo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.OdontologyDiagnosticRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.OdontologyProcedureRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.OdontologyDiagnostic;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.OdontologyProcedure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class LoadDentalActions {

    private static final Logger LOG = LoggerFactory.getLogger(LoadDentalActions.class);

    private final OdontologyProcedureRepository odontologyProcedureRepository;

    private final OdontologyDiagnosticRepository odontologyDiagnosticRepository;

    private final SnomedService snomedService;

    private final CalculateCie10Facade calculateCie10Facade;

    private final DocumentService documentService;

    public LoadDentalActions(OdontologyProcedureRepository odontologyProcedureRepository,
                             OdontologyDiagnosticRepository odontologyDiagnosticRepository,
                             SnomedService snomedService,
                             CalculateCie10Facade calculateCie10Facade,
                             DocumentService documentService) {
        this.odontologyProcedureRepository = odontologyProcedureRepository;
        this.odontologyDiagnosticRepository = odontologyDiagnosticRepository;
        this.snomedService = snomedService;
        this.calculateCie10Facade = calculateCie10Facade;
        this.documentService = documentService;
    }

    public List<Integer> run(PatientInfoBo patientInfo, Long documentId, List<DentalActionBo> dentalActionBos) {
        LOG.debug("Input parameters -> patientInfo {}, documentId {}, dentalActionBos {}", patientInfo, documentId, dentalActionBos);
        List<Integer> result = new ArrayList<>();
        dentalActionBos.forEach(dentalAction -> {
            if (dentalAction.isDiagnostic()) {
                OdontologyDiagnostic odontologyDiagnostic = getNewOdontologyDiagnostic(patientInfo, dentalAction);
                result.add(odontologyDiagnosticRepository.save(odontologyDiagnostic).getId());
                documentService.createDocumentOdontologyDiagnostic(documentId, odontologyDiagnostic.getId());
            } else {
                OdontologyProcedure odontologyProcedure = getNewOdontologyProcedure(patientInfo, dentalAction);
                result.add(odontologyProcedureRepository.save(odontologyProcedure).getId());
                documentService.createDocumentOdontologyProcedure(documentId, odontologyProcedure.getId());
            }
        });
        LOG.debug("Output -> {}", result);
        return result;
    }

    private OdontologyProcedure getNewOdontologyProcedure(PatientInfoBo patientInfo, DentalActionBo dentalProcedure) {
        OdontologyProcedure result = new OdontologyProcedure();

        Integer snomedId = snomedService.getSnomedId(dentalProcedure.getSnomed())
                .orElseGet(() -> snomedService.createSnomedTerm(dentalProcedure.getSnomed()));
        String cie10Codes = calculateCie10Facade.execute(dentalProcedure.getSnomed().getSctid(),
                new Cie10FacadeRuleFeature(patientInfo.getGenderId(), patientInfo.getAge()));

        result.setPatientId(patientInfo.getId());
        result.setSnomedId(snomedId);
        result.setCie10Codes(cie10Codes);

        if (dentalProcedure.getTooth() != null) {
            Integer toothId = snomedService.getSnomedId(dentalProcedure.getTooth()).orElse(null);
            result.setToothId(toothId);
        }

        if (dentalProcedure.getSurface() != null) {
            Integer surfaceId = snomedService.getSnomedId(dentalProcedure.getSurface()).orElse(null);
            result.setSurfaceId(surfaceId);
        }

        return result;
    }

    private OdontologyDiagnostic getNewOdontologyDiagnostic(PatientInfoBo patientInfo, DentalActionBo dentalDiagnostic) {
        OdontologyDiagnostic result = new OdontologyDiagnostic();

        Integer snomedId = snomedService.getSnomedId(dentalDiagnostic.getSnomed())
                .orElseGet(() -> snomedService.createSnomedTerm(dentalDiagnostic.getSnomed()));
        String cie10Codes = calculateCie10Facade.execute(dentalDiagnostic.getSnomed().getSctid(),
                new Cie10FacadeRuleFeature(patientInfo.getGenderId(), patientInfo.getAge()));

        result.setPatientId(patientInfo.getId());
        result.setSnomedId(snomedId);
        result.setCie10Codes(cie10Codes);

        if (dentalDiagnostic.getTooth() != null) {
            Integer toothId = snomedService.getSnomedId(dentalDiagnostic.getTooth()).orElse(null);
            result.setToothId(toothId);
        }

        if (dentalDiagnostic.getSurface() != null) {
            Integer surfaceId = snomedService.getSnomedId(dentalDiagnostic.getSurface()).orElse(null);
            result.setSurfaceId(surfaceId);
        }

        return result;
    }

}
