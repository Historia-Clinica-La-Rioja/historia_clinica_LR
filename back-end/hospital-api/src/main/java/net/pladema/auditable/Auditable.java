package net.pladema.auditable;

import java.util.Optional;

import net.pladema.auditable.entity.Audit;

public interface Auditable {
 
    Optional<Audit> getAudit();
 
    void setAudit(Audit audit);
}