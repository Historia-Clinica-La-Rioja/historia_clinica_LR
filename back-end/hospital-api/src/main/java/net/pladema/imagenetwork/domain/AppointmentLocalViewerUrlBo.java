package net.pladema.imagenetwork.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AppointmentLocalViewerUrlBo {

    Integer appointmentId;
    String localViewerUrl;
    String identificationNumber;
}
