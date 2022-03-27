package net.pladema.scheduledjobs.jobs;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.snowstorm.controller.service.UpdateSnomedGroupExternalService;
import net.pladema.snowstorm.services.exceptions.SnowstormApiException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@ConditionalOnProperty(
        value="scheduledjobs.updatesnomedcache.enabled",
        havingValue = "true",
        matchIfMissing = false)
public class UpdateSnomedCachedConceptsJob {

    private @Value("${scheduledjobs.updatesnomedcache.eclkeys:}") String eclKeys;

    private final UpdateSnomedGroupExternalService updateSnomedGroupExternalService;

    @Scheduled(cron = "${scheduledjobs.updatesnomedcache.seconds} " +
            "${scheduledjobs.updatesnomedcache.minutes} " +
            "${scheduledjobs.updatesnomedcache.hours} " +
            "${scheduledjobs.updatesnomedcache.dayofmonth} " +
            "${scheduledjobs.updatesnomedcache.month} " +
            "${scheduledjobs.updatesnomedcache.dayofweek}")
    public void execute() throws SnowstormApiException {
        log.debug("Executing UpdateSnomedCachedConceptsJob at {}", new Date());

        List<String> eclKeyList = Arrays.stream(eclKeys.split(","))
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());

        for (String eclKey : eclKeyList) {
            updateSnomedGroupExternalService.run(eclKey);
        }

        log.debug("Finished executing UpdateSnomedCachedConceptsJob at {}", new Date());
    }

}
