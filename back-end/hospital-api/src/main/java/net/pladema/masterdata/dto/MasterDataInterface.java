package net.pladema.masterdata.dto;

import java.io.Serializable;

public interface MasterDataInterface <T extends Serializable > {

    public T getId();

    public void setId(T id);

    public String getDescription();

    public void setDescription(String description);

}
