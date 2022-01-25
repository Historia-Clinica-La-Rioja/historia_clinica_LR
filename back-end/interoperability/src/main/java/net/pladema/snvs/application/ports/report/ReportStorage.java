package net.pladema.snvs.application.ports.report;

import net.pladema.snvs.domain.problem.exceptions.SnvsProblemBoException;
import net.pladema.snvs.domain.report.SnvsReportBo;

import java.util.Optional;

public interface ReportStorage {
    SnvsReportBo save(SnvsReportBo snvsReportBo);

    Optional<SnvsReportBo> findById(Integer snvsReportId) throws SnvsProblemBoException;
}
