package ar.lamansys.sgh.clinichistory.application.ports;

public interface IndicationStorage {

	boolean updateStatus(Integer id, Short statusId, Integer userId);

}
