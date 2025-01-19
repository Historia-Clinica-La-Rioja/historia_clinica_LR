package ar.lamansys.sgh.clinichistory.domain.document;

public interface IEditableDocumentBo extends IDocumentBo, IModifiableBo {

    void setDocumentStatusId(String documentStatusId);
}
