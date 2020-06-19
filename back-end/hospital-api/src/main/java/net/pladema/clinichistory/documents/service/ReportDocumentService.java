package net.pladema.clinichistory.documents.service;

import net.pladema.clinichistory.ips.service.domain.*;
import net.pladema.clinichistory.hospitalization.service.summary.domain.ResponsibleDoctorBo;

import java.util.List;

public interface ReportDocumentService {

    GeneralHealthConditionBo getReportHealthConditionFromDocument(Long documentId);

    List<InmunizationBo> getReportInmunizationStateFromDocument(Long documentId);

    List<AllergyConditionBo> getReportAllergyIntoleranceStateFromDocument(Long documentId);

    List<MedicationBo> getReportMedicationStateFromDocument(Long documentId);

    AnthropometricDataBo getReportAnthropometricDataStateFromDocument(Long documentId);

    VitalSignBo getReportVitalSignStateFromDocument(Long documentId);

    ResponsibleDoctorBo getAuthor(Long documentId);

}

