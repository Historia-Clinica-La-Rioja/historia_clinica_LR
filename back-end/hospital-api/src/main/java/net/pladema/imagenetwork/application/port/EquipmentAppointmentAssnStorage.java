package net.pladema.imagenetwork.application.port;

import net.pladema.imagenetwork.domain.AppointmentLocalViewerUrlBo;

import java.util.Collection;
import java.util.List;

public interface EquipmentAppointmentAssnStorage {

    List<AppointmentLocalViewerUrlBo> getLocalViwerUrlFromAppointmentIdList(Collection<Integer> ids);

}
