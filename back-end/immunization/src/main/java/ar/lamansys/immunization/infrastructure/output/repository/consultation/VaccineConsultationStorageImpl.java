package ar.lamansys.immunization.infrastructure.output.repository.consultation;

import ar.lamansys.immunization.domain.consultation.VaccineConsultationBo;
import ar.lamansys.immunization.domain.consultation.VaccineConsultationStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class VaccineConsultationStorageImpl implements VaccineConsultationStorage {

    private final Logger logger;

    private final VaccineConsultationRepository vaccineConsultationRepository;

    public VaccineConsultationStorageImpl(VaccineConsultationRepository vaccineConsultationRepository) {
        this.vaccineConsultationRepository = vaccineConsultationRepository;
        this.logger = LoggerFactory.getLogger(this.getClass());
    }

    @Override
    public Integer save(VaccineConsultationBo vaccineConsultationBo) {
        logger.debug("Save new VaccineConsultation {} ", vaccineConsultationBo);
        VaccineConsultation newVaccine = mapTo(vaccineConsultationBo);
        newVaccine = vaccineConsultationRepository.save(newVaccine);
        logger.debug("VaccineConsultation {} saved", newVaccine.getId());
        return newVaccine.getId();
    }

    private VaccineConsultation mapTo(VaccineConsultationBo vaccineConsultationBo) {
        return new VaccineConsultation(null,
                vaccineConsultationBo.getInstitutionId(),
                vaccineConsultationBo.getPatientId(),
                vaccineConsultationBo.getPatientMedicalCoverageId(),
                vaccineConsultationBo.getDoctorId(),
                vaccineConsultationBo.getClinicalSpecialtyId(),
                vaccineConsultationBo.getPerformedDate(),
                vaccineConsultationBo.isBillable()
                );
    }
}
