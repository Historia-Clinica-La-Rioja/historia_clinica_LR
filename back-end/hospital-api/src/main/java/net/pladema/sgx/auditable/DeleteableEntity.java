package net.pladema.sgx.auditable;

import java.time.LocalDateTime;

public interface DeleteableEntity<I> {

    public I getDeleteBy();

    public void setDeleteBy(I user);

    public LocalDateTime getDeletedOn();

    public void setDeletedOn(LocalDateTime dateTime);

    public boolean isDeleted();
    
    public void setDeleted(Boolean deleted);
}
