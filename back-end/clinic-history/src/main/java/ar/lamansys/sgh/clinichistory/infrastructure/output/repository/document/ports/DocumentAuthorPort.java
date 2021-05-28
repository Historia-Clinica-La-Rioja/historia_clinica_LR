package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.ports;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.ResponsibleDoctorVo;

// Moverlo a la capa de dominio/service.
public interface DocumentAuthorPort {

    ResponsibleDoctorVo getAuthor(Long documentId);
}
