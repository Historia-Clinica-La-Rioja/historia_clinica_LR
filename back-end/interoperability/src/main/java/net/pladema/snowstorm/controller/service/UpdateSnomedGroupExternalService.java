package net.pladema.snowstorm.controller.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.snowstorm.services.exceptions.SnowstormApiException;
import net.pladema.snowstorm.services.updateSnomedGroup.UpdateSnomedGroup;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UpdateSnomedGroupExternalService {

    private final UpdateSnomedGroup updateSnomedGroup;

    public void run(String eclKey) throws SnowstormApiException {
        log.debug("Input parameter -> eclKey {}", eclKey);
        updateSnomedGroup.run(eclKey);
    }

}
