package ar.lamansys.sgh.shared.infrastructure.input.service.snowstorm;


import ar.lamansys.sgh.shared.infrastructure.input.service.snowstorm.exceptions.SnowstormPortException;

public interface SharedSnowstormPort {

    String mapSctidToNomivacId(String sctid) throws SnowstormPortException;
}
