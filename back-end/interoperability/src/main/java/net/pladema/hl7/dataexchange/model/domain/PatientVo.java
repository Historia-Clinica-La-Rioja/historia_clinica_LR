package net.pladema.hl7.dataexchange.model.domain;

import lombok.Getter;
import lombok.Setter;
import net.pladema.hl7.dataexchange.model.adaptor.Cast;
import net.pladema.hl7.dataexchange.model.adaptor.FhirAddress;
import net.pladema.hl7.dataexchange.model.adaptor.FhirString;
import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;
import org.apache.commons.text.WordUtils;

import java.time.LocalDate;

@Getter
@Setter
public class PatientVo {

    public final static String COUNTRY="AR";

    private final BidiMap<Short, String> GENDER;

    public PatientVo(){
        GENDER = new DualHashBidiMap<>();
        GENDER.put((short)1, "female");
        GENDER.put((short)2, "male");
    }

    public PatientVo(Object[] tuple){
        this();
        int index = 0;
        setFirstname(Cast.toString(tuple[index++]));
        setMiddlenames(Cast.toString(tuple[index++]));
        setLastname(Cast.toString(tuple[index++]));
        setOtherLastName(Cast.toString(tuple[index++]), Cast.toString(tuple[index++]));
        setIdentificationNumber(Cast.toString(tuple[index++]));
        Short gender = Cast.toShort(tuple[index++]);
        setGender(GENDER.getOrDefault(gender, null));
        setBirthdate(Cast.toLocalDate(tuple[index++]));
        setPhoneNumber(Cast.toString(tuple[index]));
    }

    private String identificationNumber;
    //local (domain) identifier value
    private String id;

    //Father surname
    private String lastname="";
    //Mather surname
    private String otherLastName;
    //First name
    private String firstname;
    //Second name
    private String middlenames;

    private FhirAddress fullAddress;

    //phone
    private String phoneNumber;

    private LocalDate birthdate;

    private Short gender;

    public String getGender(){
        return GENDER.get(gender);
    }

    public String getFullName(){
        return FhirString.joining(lastname, firstname, middlenames);
    }

    public String getBirthdate(){
        return birthdate.toString();
    }

    public void setOtherLastName(String otherLastName, String motherLastName){
        if(otherLastName != null)
            this.otherLastName=otherLastName;
        else
            //if not set, has to take the mather lastname
            this.otherLastName=motherLastName;
    }

    public void setGender(String gender){
        this.gender = GENDER.getKey(gender);
    }

    public boolean hasOtherLastName() {
        return otherLastName != null && !otherLastName.isBlank();
    }

    public boolean hasPhoneNumber(){
        return phoneNumber != null && !phoneNumber.isBlank();
    }

    public boolean hasBirthDateData() {
        return birthdate != null;
    }

    public boolean hasAddressData(){
        return fullAddress != null && fullAddress.hasAddressData();
    }

    public boolean hasMiddlenamesData(){
        return middlenames != null && !middlenames.isBlank();
    }

    public String getSummary(){
        return "Resumen de paciente de ".concat(WordUtils.capitalizeFully(getFullName()));
    }
}
