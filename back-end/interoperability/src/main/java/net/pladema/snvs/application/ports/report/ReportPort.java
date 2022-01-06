package net.pladema.snvs.application.ports.report;

import net.pladema.snvs.application.ports.report.exceptions.ReportPortException;
import net.pladema.snvs.domain.report.SnvsReportBo;
import net.pladema.snvs.domain.report.SnvsToReportBo;

public interface ReportPort {

    SnvsReportBo run(SnvsToReportBo buildReport) throws ReportPortException;
}
