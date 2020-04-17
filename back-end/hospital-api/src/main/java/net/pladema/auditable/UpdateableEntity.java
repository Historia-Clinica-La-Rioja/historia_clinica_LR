package net.pladema.auditable;

import java.time.LocalDateTime;

public interface UpdateableEntity<I> {

    public I getModifiedBy();

    public void setModifiedBy(I user);

    public LocalDateTime getUpdatedOn();

    public void setUpdatedOn(LocalDateTime dateTime);
}
