package net.pladema.imagenetwork.infrastructure.output.database;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.imagenetwork.application.port.EquipmentAppointmentAssnStorage;
import net.pladema.imagenetwork.domain.AppointmentLocalViewerUrlBo;
import net.pladema.medicalconsultation.appointment.repository.EquipmentAppointmentAssnRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Repository
public class EquipmentAppointmentAssnStorageImpl implements EquipmentAppointmentAssnStorage {

    private final EquipmentAppointmentAssnRepository repository;

    @Override
    public List<AppointmentLocalViewerUrlBo> getLocalViwerUrlFromAppointmentIdList(Collection<Integer> ids) {
        return repository.findLocalViewerUrlFromAppointmentIdList(ids);
    }
}
