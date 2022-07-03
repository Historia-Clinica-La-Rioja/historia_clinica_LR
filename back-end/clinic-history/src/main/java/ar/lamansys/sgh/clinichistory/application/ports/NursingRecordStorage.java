package ar.lamansys.sgh.clinichistory.application.ports;

import ar.lamansys.sgh.clinichistory.domain.ips.IndicationSummaryBo;

import java.util.List;


public interface NursingRecordStorage {

	List<Integer> createNursingRecordsFromIndication(IndicationSummaryBo indication);

}
