package net.pladema.staff.application.gethealthcareprofessional;

import net.pladema.staff.service.domain.HealthcareProfessionalBo;

public interface GetHealthcareProfessional {
    HealthcareProfessionalBo execute(Integer personId);
}
