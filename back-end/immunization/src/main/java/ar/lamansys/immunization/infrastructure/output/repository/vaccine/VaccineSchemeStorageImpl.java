package ar.lamansys.immunization.infrastructure.output.repository.vaccine;

import ar.lamansys.immunization.domain.vaccine.VaccineSchemeStorage;
import org.springframework.stereotype.Service;

@Service
public class VaccineSchemeStorageImpl implements VaccineSchemeStorage {

    private final VaccineSchemeRepository vaccineSchemeRepository;

    public VaccineSchemeStorageImpl(VaccineSchemeRepository vaccineSchemeRepository) {
        this.vaccineSchemeRepository = vaccineSchemeRepository;
    }

    @Override
    public boolean isValidScheme(Short id) {
        return vaccineSchemeRepository.existsById(id);
    }
}
