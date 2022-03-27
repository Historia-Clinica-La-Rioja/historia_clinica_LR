package net.pladema.scheduledjobs.jobs;

import net.pladema.person.controller.mapper.EthnicityMapper;
import net.pladema.person.controller.service.PersonMasterDataExternalService;
import net.pladema.snowstorm.services.SnowstormService;
import net.pladema.snowstorm.services.domain.SnowstormSearchResponse;
import net.pladema.snowstorm.services.exceptions.SnowstormApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@ConditionalOnProperty(
        value="scheduledjobs.updateethnicities.enabled",
        havingValue = "true",
        matchIfMissing = false)
public class UpdateEthnicityMasterDataJob {

    private static final Logger LOG = LoggerFactory.getLogger(UpdateEthnicityMasterDataJob.class);

    private static final String ECL = "^93491000221103 |conjunto de referencias simples de pueblos originarios argentinos (metadato fundacional)|";

    private final SnowstormService snowstormService;

    private final EthnicityMapper ethnicityMapper;

    private final PersonMasterDataExternalService personMasterDataExternalService;

    public UpdateEthnicityMasterDataJob(SnowstormService snowstormService,
                                        EthnicityMapper ethnicityMapper,
                                        PersonMasterDataExternalService personMasterDataExternalService) {
        this.snowstormService = snowstormService;
        this.ethnicityMapper = ethnicityMapper;
        this.personMasterDataExternalService = personMasterDataExternalService;
    }

    @Scheduled(cron = "${scheduledjobs.updateethnicities.seconds} " +
            "${scheduledjobs.updateethnicities.minutes} " +
            "${scheduledjobs.updateethnicities.hours} " +
            "${scheduledjobs.updateethnicities.dayofmonth} " +
            "${scheduledjobs.updateethnicities.month} " +
            "${scheduledjobs.updateethnicities.dayofweek}")
    public void execute() throws SnowstormApiException {
        LOG.debug("Executing UpdateEthnicityMasterDataJob at {}", new Date());
        SnowstormSearchResponse response = snowstormService.getConceptsByEcl(ECL);
        personMasterDataExternalService.updateActiveEthnicities(ethnicityMapper.fromSnowstormItemResponseList(response.getItems()));
        LOG.debug("Finishing UpdateEthnicityMasterDataJob at {}", new Date());
    }

}
