package ar.lamansys.sgx.shared.auditable;

import java.time.LocalDateTime;

public interface CreationableEntity<I> {

    I getCreatedBy();

    void setCreatedBy(I user);

    LocalDateTime getCreatedOn();

    void setCreatedOn(LocalDateTime dateTime);
}
