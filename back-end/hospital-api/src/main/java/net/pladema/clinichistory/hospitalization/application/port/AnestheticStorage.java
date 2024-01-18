package net.pladema.clinichistory.hospitalization.application.port;

import net.pladema.clinichistory.hospitalization.domain.AnestheticReportBo;

public interface AnestheticStorage {
    Integer save(AnestheticReportBo anestheticReport);
}
