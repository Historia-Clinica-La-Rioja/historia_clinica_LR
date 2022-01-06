package net.pladema.snvs.infrastructure.output.repository.doctor;

import ar.lamansys.sgh.shared.infrastructure.input.service.SharedStaffPort;
import ar.lamansys.sgx.shared.security.UserInfo;
import net.pladema.snvs.application.ports.doctor.SnvsDoctorStorage;
import net.pladema.snvs.domain.doctor.DoctorInfoBo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SnvsDoctorStorageImpl implements SnvsDoctorStorage {

    private final Logger logger;

    private final SharedStaffPort sharedStaffPort;

    public SnvsDoctorStorageImpl(SharedStaffPort sharedStaffPort) {
        this.sharedStaffPort = sharedStaffPort;
        this.logger = LoggerFactory.getLogger(this.getClass());
    }

    @Override
    public Optional<DoctorInfoBo> getDoctorInfo() {
        logger.debug("Fetch external doctor information");
        return Optional
                .ofNullable(sharedStaffPort.getProfessionalCompleteInfo(UserInfo.getCurrentAuditor()))
                .map(profesional -> new DoctorInfoBo(profesional.getId()));
    }
}
