package net.pladema.sgx.auditable;

import java.util.Optional;

import net.pladema.sgx.auditable.entity.Audit;

public interface Auditable {
 
    Optional<Audit> getAudit();
 
    void setAudit(Audit audit);
}