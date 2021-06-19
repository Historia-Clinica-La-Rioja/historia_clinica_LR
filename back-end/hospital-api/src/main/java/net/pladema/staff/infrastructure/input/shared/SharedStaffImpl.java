package net.pladema.staff.infrastructure.input.shared;

import net.pladema.staff.service.domain.ClinicalSpecialtyBo;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedStaffPort;
import ar.lamansys.sgh.shared.infrastructure.input.service.ClinicalSpecialtyDto;
import net.pladema.staff.service.ClinicalSpecialtyService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SharedStaffImpl implements SharedStaffPort {

    private final ClinicalSpecialtyService clinicalSpecialtyService;

    public SharedStaffImpl(ClinicalSpecialtyService clinicalSpecialtyService) {
        this.clinicalSpecialtyService = clinicalSpecialtyService;
    }


    @Override
    public Optional<ClinicalSpecialtyDto> getClinicalSpecialty(Integer clinicalSpecialtyId) {
        return clinicalSpecialtyService
                .getClinicalSpecialty(clinicalSpecialtyId)
                .map(this::toClinicalSpecialtyDto);
    }

    private ClinicalSpecialtyDto toClinicalSpecialtyDto(ClinicalSpecialtyBo clinicalSpecialtyBo) {
        return new ClinicalSpecialtyDto(clinicalSpecialtyBo.getId(), clinicalSpecialtyBo.getName());
    }
}
