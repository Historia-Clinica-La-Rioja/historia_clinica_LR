package net.pladema.sgh.app.seeds.data;

import lombok.Getter;
import lombok.Setter;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class InstitutionInfoSeed {

    private String name;

    private AddressInfoSeed address;

    private String phone;

    private String email;

    private String cuit;

    private String sisaCode;

    private List<UserInfoSeed> users = new ArrayList<>();
}
