package ar.lamansys.sgh.clinichistory.application.createDocument;


import ar.lamansys.sgh.clinichistory.domain.document.IDocumentBo;

public interface DocumentFactory {

    Long run(IDocumentBo d, boolean createFile);
}
