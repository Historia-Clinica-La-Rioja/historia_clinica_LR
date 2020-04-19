package net.pladema.masterdata.dto;

public interface MasterDataInterface <T> {

    public T getId();

    public void setId(T id);

    public String getDescription();

    public void setDescription(String description);

}
