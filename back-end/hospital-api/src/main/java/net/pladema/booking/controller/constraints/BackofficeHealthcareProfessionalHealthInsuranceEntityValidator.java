package net.pladema.booking.controller.constraints;

import net.pladema.booking.repository.entity.VProfessionalMedicalCoverage;
import net.pladema.sgx.backoffice.rest.BackofficeEntityValidatorAdapter;
import net.pladema.sgx.exceptions.BackofficeValidationException;
import net.pladema.booking.repository.HealthcareProfessionalHealthInsuranceRepository;
import org.springframework.stereotype.Component;

@Component
public class BackofficeHealthcareProfessionalHealthInsuranceEntityValidator
        extends BackofficeEntityValidatorAdapter<VProfessionalMedicalCoverage, Integer> {

    private final HealthcareProfessionalHealthInsuranceRepository repository;

    public BackofficeHealthcareProfessionalHealthInsuranceEntityValidator(HealthcareProfessionalHealthInsuranceRepository repository) {
        this.repository = repository;
    }

    @Override
    public void assertCreate(VProfessionalMedicalCoverage entity) {
        if(repository.findByProfessionalIdAndMedicalCoverageId(entity.getHealthcareProfessionalId(),
                entity.getMedicalCoverageId()) > 0){
            throw new BackofficeValidationException("healthcareprofessional.healthinsurance");
        }
    }
}
