package ar.lamansys.sgx.shared.auditable;

import java.time.LocalDateTime;

public interface UpdateableEntity<I> {

    I getUpdatedBy();

    void setUpdatedBy(I user);

    LocalDateTime getUpdatedOn();

    void setUpdatedOn(LocalDateTime dateTime);
}
