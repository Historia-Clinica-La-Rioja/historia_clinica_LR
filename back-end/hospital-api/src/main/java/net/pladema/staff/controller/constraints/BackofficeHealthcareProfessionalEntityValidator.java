package net.pladema.staff.controller.constraints;

import net.pladema.staff.repository.HealthcareProfessionalRepository;
import net.pladema.staff.repository.entity.HealthcareProfessional;
import net.pladema.sgx.backoffice.rest.BackofficeEntityValidatorAdapter;
import net.pladema.sgx.exceptions.BackofficeValidationException;
import org.springframework.stereotype.Component;

@Component
public class BackofficeHealthcareProfessionalEntityValidator extends BackofficeEntityValidatorAdapter<HealthcareProfessional, Integer> {

    HealthcareProfessionalRepository healthcareProfessionalRepository;

    public BackofficeHealthcareProfessionalEntityValidator(HealthcareProfessionalRepository healthcareProfessionalRepository) {
        this.healthcareProfessionalRepository = healthcareProfessionalRepository;
    }

    @Override
    public void assertUpdate(Integer id, HealthcareProfessional entity) {
        //nothing to do
    }

    @Override
    public void assertCreate(HealthcareProfessional entity) {
        if(healthcareProfessionalRepository.findProfessionalByPersonId(entity.getPersonId()).isPresent()){
            throw new BackofficeValidationException("healthcareprofessional.exists");
        }
    }

}
