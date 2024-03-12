package net.pladema.clinichistory.hospitalization.service.documents.validation;

import ar.lamansys.sgh.clinichistory.domain.ips.PostAnesthesiaStatusBo;
import ar.lamansys.sgh.clinichistory.domain.ips.enums.EIntermentPlace;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class PostAnesthesiaStatusValidator {

    public void assertInternmentPlace(PostAnesthesiaStatusBo postAnesthesiaStatus) {
        Short internmentPlaceId = postAnesthesiaStatus.getInternmentPlace();
        if (internmentPlaceId == null)
            return;
        EIntermentPlace.map(internmentPlaceId);
    }
}
