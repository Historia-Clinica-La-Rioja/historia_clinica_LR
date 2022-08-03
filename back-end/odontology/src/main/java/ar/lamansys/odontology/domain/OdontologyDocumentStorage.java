package ar.lamansys.odontology.domain;

import ar.lamansys.odontology.domain.consultation.OdontologyDocumentBo;

public interface OdontologyDocumentStorage {

    Long save(OdontologyDocumentBo odontologyDocumentBo);

}
