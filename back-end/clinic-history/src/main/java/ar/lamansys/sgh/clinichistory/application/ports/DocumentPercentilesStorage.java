package ar.lamansys.sgh.clinichistory.application.ports;

public interface DocumentPercentilesStorage {

	Integer save(Long documentId, Integer percentilesId, Double percentilesValue);

}
