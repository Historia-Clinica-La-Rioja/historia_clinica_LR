package net.pladema.clinichistory.requests.servicerequests.repository.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
@AllArgsConstructor
public class DiagnosticReportFilterVo {
    private final Integer patientId;
    private final String status;
    private final String study;
    private final String healthCondition;
    private final String category;
    private final List<String> categoriesToBeExcluded;
	private final Integer userId;
	private final List<Short> loggedUserRoleIds;
}
