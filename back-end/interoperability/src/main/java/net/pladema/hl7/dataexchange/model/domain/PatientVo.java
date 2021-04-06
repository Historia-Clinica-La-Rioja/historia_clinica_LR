package net.pladema.hl7.dataexchange.model.domain;

import lombok.Getter;
import lombok.Setter;
import net.pladema.hl7.dataexchange.model.adaptor.FhirAddress;
import net.pladema.hl7.dataexchange.model.adaptor.FhirDateMapper;
import net.pladema.hl7.dataexchange.model.adaptor.FhirString;
import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;
import org.apache.commons.text.WordUtils;

import java.time.LocalDate;

@Getter
@Setter
public class PatientVo {

    public static final String COUNTRY="AR";

    private final BidiMap<Short, String> genderCoding;

    public PatientVo(){
        genderCoding = new DualHashBidiMap<>();
        genderCoding.put((short)1, "female");
        genderCoding.put((short)2, "male");
    }

    public PatientVo(String firstname, String middlenames, String lastname, String otherLastName,
                     String mothersLastName, String identificationNumber, Short genderId,
                     String birthDate, String phoneNumber, Integer addressId){
        this();
        setFirstname(firstname);
        setMiddlenames(middlenames);
        setLastname(lastname);
        setOtherLastName(otherLastName, mothersLastName);
        setIdentificationNumber(identificationNumber);
        setGender(genderCoding.getOrDefault(genderId, null));
        setBirthdate(FhirDateMapper.toLocalDate(birthDate));
        setPhoneNumber(phoneNumber);
        setAddressId(addressId);
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

    private Integer addressId;
    private FhirAddress fullAddress;

    //phone
    private String phoneNumber;

    private LocalDate birthdate;

    private Short gender;

    public String getGender(){
        return genderCoding.get(gender);
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
        this.gender = genderCoding.getKey(gender);
    }

    public boolean hasOtherLastName() {
        return FhirString.hasText(otherLastName);
    }

    public boolean hasBirthDateData() {
        return birthdate != null;
    }

    public boolean hasAddressData(){
        return fullAddress != null && fullAddress.hasAddressData();
    }

    public boolean hasMiddlenamesData(){
        return FhirString.hasText(middlenames);
    }

    public String getSummary(){
        return "Resumen de paciente de ".concat(WordUtils.capitalizeFully(getFullName()));
    }
}
