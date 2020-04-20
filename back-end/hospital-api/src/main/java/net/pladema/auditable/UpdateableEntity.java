package net.pladema.auditable;

import java.time.LocalDateTime;

public interface UpdateableEntity<I> {

    public I getUpdatedBy();

    public void setUpdatedBy(I user);

    public LocalDateTime getUpdatedOn();

    public void setUpdatedOn(LocalDateTime dateTime);
}
