package ar.lamansys.sgx.shared.masterdata.infrastructure.input.rest.dto;

import lombok.ToString;

import java.io.Serializable;

@ToString
public abstract class AbstractMasterdataDto<T extends Serializable> implements MasterDataInterface <T>, Serializable {

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
