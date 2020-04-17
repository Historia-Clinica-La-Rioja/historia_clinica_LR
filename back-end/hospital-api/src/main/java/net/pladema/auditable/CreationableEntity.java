package net.pladema.auditable;

import java.time.LocalDateTime;

public interface CreationableEntity<I> {

    public I getCreatedBy();

    public void setCreatedBy(I user);

    public LocalDateTime getCreatedOn();

    public void setCreatedOn(LocalDateTime dateTime);
}
