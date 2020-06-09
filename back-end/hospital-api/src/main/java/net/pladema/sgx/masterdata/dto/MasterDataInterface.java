package net.pladema.sgx.masterdata.dto;

import java.io.Serializable;

public interface MasterDataInterface <T extends Serializable > {

    T getId();

    void setId(T id);

    String getDescription();

    void setDescription(String description);

}
