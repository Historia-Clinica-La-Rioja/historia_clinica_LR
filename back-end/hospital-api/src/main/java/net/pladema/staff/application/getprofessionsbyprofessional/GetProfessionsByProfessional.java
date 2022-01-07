package net.pladema.staff.application.getprofessionsbyprofessional;

import net.pladema.staff.service.domain.HealthcareProfessionalSpecialtyBo;

import java.util.List;

public interface GetProfessionsByProfessional {

    List<HealthcareProfessionalSpecialtyBo> execute(Integer professionalId);
}
