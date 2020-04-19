package net.pladema.masterdata.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
public abstract class MasterdataDto <T> implements MasterDataInterface <T>{

    private T id;

    private String description;

    @Override
    public T getId(){
        return id;
    }

    @Override
    public void setId(T id) {
        this.id = id;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

}
