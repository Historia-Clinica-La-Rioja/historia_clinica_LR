package ar.lamansys.sgh.clinichistory.application.fetchHCE;

import ar.lamansys.sgh.clinichistory.domain.hce.HCEToothRecordBo;
import ar.lamansys.sgh.clinichistory.domain.hce.HCEToothRecordStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HCEToothRecordServiceImpl implements HCEToothRecordService {

    private static final Logger LOG = LoggerFactory.getLogger(HCEToothRecordServiceImpl.class);

    private static final String LOGGING_OUTPUT = "Output -> {}";

    private final HCEToothRecordStorage hceToothRecordStorage;

    public HCEToothRecordServiceImpl(HCEToothRecordStorage hceToothRecordStorage) {
        this.hceToothRecordStorage = hceToothRecordStorage;
    }

    @Override
    public List<HCEToothRecordBo> getToothRecords(Integer patientId, String toothSctid) {
        LOG.debug("Input parameters -> patientId {}, toothSctid {}", patientId, toothSctid);
        List<HCEToothRecordBo> result = hceToothRecordStorage.getToothRecords(patientId, toothSctid);
        LOG.debug("Output size -> {}", result.size());
        LOG.trace(LOGGING_OUTPUT, result);
        return result;
    }
}
