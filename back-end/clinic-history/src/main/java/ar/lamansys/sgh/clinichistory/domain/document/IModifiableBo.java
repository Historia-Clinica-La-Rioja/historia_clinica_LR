package ar.lamansys.sgh.clinichistory.domain.document;

public interface IModifiableBo {
    String getModificationReason();
    void setModificationReason(String reason);
    Integer getCreatedBy();
    void setCreatedBy(Integer createdBy);
}
