package net.pladema.medicalconsultation.appointment.service.impl;

import net.pladema.medicalconsultation.appointment.repository.AppointmentStateRepository;
import net.pladema.medicalconsultation.appointment.service.AppointmentMasterDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class AppointmentMasterDataServiceImpl implements AppointmentMasterDataService {

    public static final String OUTPUT = "Output -> {}";
    private static final Logger LOG = LoggerFactory.getLogger(AppointmentMasterDataServiceImpl.class);

    private final AppointmentStateRepository appointmentRepository;

    public AppointmentMasterDataServiceImpl(AppointmentStateRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    @Override
    public boolean validAppointmentStateId(short appointmentStateId) {
        LOG.debug("Input parameters -> appointmentStateId {}", appointmentStateId);
        boolean result = appointmentRepository.existsById(appointmentStateId);
        LOG.debug(OUTPUT, result);
        return result;
    }
}
