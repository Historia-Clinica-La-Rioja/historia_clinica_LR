package net.pladema.clinichistory.outpatient.createoutpatient.service;

import net.pladema.clinichistory.outpatient.createoutpatient.service.domain.OutpatientBo;
import net.pladema.clinichistory.outpatient.repository.OutpatientConsultationRepository;
import net.pladema.clinichistory.outpatient.repository.domain.OutpatientConsultation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class CreateOutpatientConsultationServiceImpl implements CreateOutpatientConsultationService{

    private static final Logger LOG = LoggerFactory.getLogger(CreateOutpatientConsultationServiceImpl.class);

    public static final String OUTPUT = "Output -> {}";

    private final OutpatientConsultationRepository outpatientConsultationRepository;

    public CreateOutpatientConsultationServiceImpl(OutpatientConsultationRepository outpatientConsultationRepository) {
        this.outpatientConsultationRepository = outpatientConsultationRepository;
    }


    @Override
    public OutpatientBo create(Integer institutionId, Integer patientId, Integer doctorId, boolean billable,
                               Integer clinicalSpecialtyId, Integer patientMedicalCoverageId) {
        LOG.debug("Input parameters institution {}, patientId {}, billable {}", institutionId, patientId, billable);
        OutpatientConsultation newOC = new OutpatientConsultation(institutionId, patientId, doctorId,
                billable, clinicalSpecialtyId, patientMedicalCoverageId);
        newOC = outpatientConsultationRepository.save(newOC);
        OutpatientBo result = createOutpatientBo(newOC);
        LOG.debug(OUTPUT, result);
        return result;
    }

    private OutpatientBo createOutpatientBo(OutpatientConsultation source) {
        LOG.debug("Input parameters outpatientConsultation {}", source);
        OutpatientBo result = new OutpatientBo();
        result.setId(source.getId());
        result.setPatientId(source.getPatientId());
        result.setBillable(source.getBillable());
        result.setDoctorId(source.getDoctorId());
        result.setInstitutionId(source.getInstitutionId());
        result.setStartDate(source.getStartDate());
        result.setClinicalSpecialtyId(source.getClinicalSpecialtyId());
        LOG.debug(OUTPUT, result);
        return result;
    }
}
