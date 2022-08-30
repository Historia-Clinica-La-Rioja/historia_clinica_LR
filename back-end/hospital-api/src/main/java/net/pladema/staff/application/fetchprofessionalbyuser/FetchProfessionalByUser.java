package net.pladema.staff.application.fetchprofessionalbyuser;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.staff.application.ports.HealthcareProfessionalStorage;
import net.pladema.staff.domain.ProfessionalCompleteBo;

@Slf4j
@Service
@RequiredArgsConstructor
public class FetchProfessionalByUser {

    private final HealthcareProfessionalStorage healthcareProfessionalStorage;

    public ProfessionalCompleteBo execute(Integer userId) {
        return healthcareProfessionalStorage.fetchProfessionalByUserId(userId);
    }
}
