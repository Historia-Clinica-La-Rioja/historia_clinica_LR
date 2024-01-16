package net.pladema.clinichistory.requests.servicerequests.domain;

import java.time.LocalDate;
import java.util.List;

public interface IServiceRequestBo {

    Integer getServiceRequestId();

    LocalDate getReportDate();

    List<String> getProblemsPt();

    List<String> getStudies();

    List<String> getCie10Codes();

    Short getAssociatedSourceTypeId();

    boolean isTranscribed();
}
