package net.pladema.snvs.infrastructure.output.repository.report;

import net.pladema.snvs.application.ports.report.ReportStorage;
import net.pladema.snvs.domain.problem.SnvsProblemBo;
import net.pladema.snvs.domain.problem.exceptions.SnvsProblemBoException;
import net.pladema.snvs.domain.report.SnvsReportBo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class ReportStorageImpl implements ReportStorage {

    private static final Logger LOG = LoggerFactory.getLogger(ReportStorageImpl.class);

    private final SnvsReportRepository snvsReportRepository;

    public ReportStorageImpl(SnvsReportRepository snvsReportRepository) {
        this.snvsReportRepository = snvsReportRepository;
    }

    @Override
    public SnvsReportBo save(SnvsReportBo snvsReportBo) {
        SnvsReport entity = mapEntity(snvsReportBo);
        entity = snvsReportRepository.save(entity);
        snvsReportBo.setId(entity.getId());
        return snvsReportBo;
    }

    @Override
    public Optional<SnvsReportBo> findById(Integer snvsReportId) throws SnvsProblemBoException {
        LOG.debug("Input parameters -> snvsReportId {}", snvsReportId);
        return snvsReportRepository.findById(snvsReportId).map(this::mapReportBo);
    }

    private SnvsReport mapEntity(SnvsReportBo snvsReportBo) {
        var result = new SnvsReport();
        result.setEventId(snvsReportBo.getEventId());
        result.setGroupEventId(snvsReportBo.getGroupEventId());
        result.setManualClassificationId(snvsReportBo.getManualClassificationId());
        result.setPatientId(snvsReportBo.getPatientId());
        result.setInstitutionId(snvsReportBo.getInstitutionId());
        result.setResponseCode(snvsReportBo.getResponseCode());
        result.setLastUpdate(snvsReportBo.getLastUpdate());
        result.setProfessionalId(snvsReportBo.getProfessionalId());
        result.setSisaRegisteredId(snvsReportBo.getSisaRegisteredId());
        result.setStatus(snvsReportBo.getStatus());
        result.setSnomedSctid(snvsReportBo.getProblemBo().getSctid());
        result.setSnomedPt(snvsReportBo.getProblemBo().getPt());
        result.setId(snvsReportBo.getId());
        return result;
    }

    private SnvsReportBo mapReportBo(SnvsReport snvsReport) throws SnvsProblemBoException {
        var result = new SnvsReportBo();
        result.setId(snvsReport.getId());
        result.setGroupEventId(snvsReport.getGroupEventId());
        result.setEventId(snvsReport.getEventId());
        result.setManualClassificationId(snvsReport.getManualClassificationId());
        result.setPatientId(snvsReport.getPatientId());
        result.setStatus(snvsReport.getStatus());
        result.setResponseCode(snvsReport.getResponseCode());
        result.setProfessionalId(snvsReport.getProfessionalId());
        result.setInstitutionId(snvsReport.getInstitutionId());
        result.setSisaRegisteredId(snvsReport.getSisaRegisteredId());
        result.setLastUpdate(snvsReport.getLastUpdate());
        result.setProblemBo(new SnvsProblemBo(snvsReport.getSnomedSctid(), snvsReport.getSnomedPt()));
        return result;
    }

}
