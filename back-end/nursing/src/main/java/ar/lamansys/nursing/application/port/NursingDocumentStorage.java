package ar.lamansys.nursing.application.port;

import ar.lamansys.nursing.domain.document.NursingDocumentBo;

public interface NursingDocumentStorage {

    Long save(NursingDocumentBo nursingDocumentBo);

}
