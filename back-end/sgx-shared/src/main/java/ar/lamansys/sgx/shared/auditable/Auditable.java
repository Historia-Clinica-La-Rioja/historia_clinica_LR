package ar.lamansys.sgx.shared.auditable;

import java.util.Optional;

import ar.lamansys.sgx.shared.auditable.entity.Audit;

public interface Auditable {
 
    Optional<Audit> getAudit();
 
    void setAudit(Audit audit);
}