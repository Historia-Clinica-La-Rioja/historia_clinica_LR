package net.pladema.snvs.application.ports.report;

import net.pladema.snvs.domain.report.SnvsReportBo;

public interface ReportStorage {
    SnvsReportBo save(SnvsReportBo snvsReportBo);
}
