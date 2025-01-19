package net.pladema.imagenetwork.application.getlocalviewerurl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.imagenetwork.application.port.EquipmentAppointmentAssnStorage;
import net.pladema.imagenetwork.domain.AppointmentLocalViewerUrlBo;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
@Service
public class GetLocalViewerUrl {

    private final static String DNI_CHAR_SEQUENCE_TO_REPLACE = "{dni}";

    private final EquipmentAppointmentAssnStorage equipmentAppointmentAssnStorage;

    public Map<Integer,String> run(Collection<Integer> appointmentIds) {
        log.debug("Input parameters -> appointmentIds {}", appointmentIds);
        var resultList = equipmentAppointmentAssnStorage.getLocalViwerUrlFromAppointmentIdList(appointmentIds);
        var localViewerUrlMapped =
                resultList
                        .stream()
                        .filter(appointmenUrlBo -> Objects.nonNull(appointmenUrlBo.getLocalViewerUrl()))
                        .collect(Collectors.toMap(AppointmentLocalViewerUrlBo::getAppointmentId,this::completeLocalViewerUrl));
        log.debug("Output ->  localViewerUrlMapped {}",localViewerUrlMapped);
        return localViewerUrlMapped;
    }

    private String completeLocalViewerUrl(AppointmentLocalViewerUrlBo appointmenUrlBo) {
        String dni = Objects.isNull(appointmenUrlBo.getIdentificationNumber()) ? "" : appointmenUrlBo.getIdentificationNumber();
        return appointmenUrlBo.getLocalViewerUrl().toLowerCase().replace(DNI_CHAR_SEQUENCE_TO_REPLACE,dni);
    }
}
