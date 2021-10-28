package net.pladema.snowstorm.services.domain;

import net.pladema.snowstorm.services.domain.semantics.SnomedECL;
import net.pladema.snowstorm.services.domain.semantics.SnomedSemantics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FetchAllSnomedEcl {

    private final Logger logger;

    private final SnomedSemantics snomedSemantics;

    public FetchAllSnomedEcl(SnomedSemantics snomedSemantics) {
        this.logger = LoggerFactory.getLogger(this.getClass());
        this.snomedSemantics = snomedSemantics;
    }

    public List<SnomedECLBo> run(){
        logger.debug("Fetch all snomed ecl");
        return Arrays.stream(SnomedECL.values()).map(this::map).collect(Collectors.toList());
    }

    private SnomedECLBo map(SnomedECL snomedECL) {
        return new SnomedECLBo(snomedECL, snomedSemantics.getEcl(snomedECL));
    }
}
