package net.pladema.hl7.dataexchange;

import net.pladema.hl7.dataexchange.model.adaptor.FhirAddress;
import net.pladema.hl7.dataexchange.model.adaptor.FhirCode;
import net.pladema.hl7.supporting.exchange.database.FhirPersistentStore;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Address;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.CodeType;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.ContactPoint;
import org.hl7.fhir.r4.model.Extension;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Meta;
import org.hl7.fhir.r4.model.Quantity;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.Resource;
import org.hl7.fhir.r4.model.ResourceType;
import org.hl7.fhir.r4.model.StringType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Arrays;

@Component
public abstract class IResourceFhir {

    private static final String DELIMITER = "/";

    private static String dominio;
    private static String sisaCode;
    private static String systemName;

    protected FhirPersistentStore store;

    protected IResourceFhir(FhirPersistentStore store){
        super();
        this.store=store;
    }

    @Value("${ws.federar.claims.iss}")
    public void setDominio(String dominio){
        IResourceFhir.dominio = dominio;
    }

    @Value("${ws.federar.sisaCode}")
    public void setSisaCode(String sisaCode){
        IResourceFhir.sisaCode = sisaCode;
    }

    @Value("${system.name:Historia de Salud Integrada}")
    public void setSystemName(String systemName){
        IResourceFhir.systemName = systemName;
    }

    public static String getDominio(){
        return IResourceFhir.dominio;
    }

    public static String getSisaCode(){
        return IResourceFhir.sisaCode;
    }

    public static String getSystemName(){
        return IResourceFhir.systemName;
    }

    public abstract ResourceType getResourceType();

    public static <R extends IBaseResource> Bundle.BundleEntryComponent fetchEntry(R resource) {
        return new Bundle.BundleEntryComponent()
                .setFullUrl(fullDomainUrl(resource))
                .setResource((Resource) resource);
    }

    public static <R extends IBaseResource> Bundle.BundleEntryComponent fetchRequestEntry(R resource) {
        return new Bundle.BundleEntryComponent()
                .setFullUrl(fullRequestUrl(resource))
                .setResource((Resource) resource);
    }

    public static String decodeCode(CodeableConcept codeableConcept){
        if (codeableConcept != null && codeableConcept.hasCoding()) {
            Coding coding = codeableConcept.getCoding().get(0);
            if(coding.hasCode())
                return coding.getCode();
        }
        return null;
    }

    public static Pair<String,String> decodeCoding(CodeableConcept codeableConcept){
        if (codeableConcept != null && codeableConcept.hasCoding()) {
            Coding coding = codeableConcept.getCoding().get(0);
            if(coding.hasCode() && coding.hasDisplay())
                return new ImmutablePair<>(coding.getCode(), coding.getDisplay());
        }
        return new ImmutablePair<>(null, null);
    }

    public static FhirAddress decodeAddress(Address address){
        FhirAddress data = new FhirAddress(address.getUse());
        if(address.hasLine()) {
            StringType fullAddress = address.getLine().get(0);
            data.setAddress(fullAddress.getValue());
        }
        data.setCity(address.getCity())
                .setProvince(address.getState())
                .setCountry(address.getCountry())
                .setPostcode(address.getPostalCode());
        return data;
    }

    public static <R extends IBaseResource> String fullDomainUrl(R resource){
        return fullUrl(resource, url(resource, dominio));
    }

    public static <R extends IBaseResource> String fullRequestUrl(R resource){
        return fullUrl(resource, url(resource, currentServletMapping()));
    }

    private static <R extends IBaseResource> String fullUrl(R resource, String url){
        String id = resource.getIdElement().getValue();
        if(id != null)
            return url.concat(DELIMITER).concat(id);
        return url;
    }

    public static Address newAddress(FhirAddress fhirAddress){
        return new Address().setUse(Address.AddressUse.WORK)
                .addLine(fhirAddress.getAddress())
                .setCity(fhirAddress.getCity())
                .setCountry(fhirAddress.getCountry())
                .setState(fhirAddress.getProvince())
                .setPostalCode(fhirAddress.getPostcode());
    }

    public static CodeableConcept newCodeableConcept(String system, FhirCode code) {
        if(code.hasCode())
            return newCodeableConcept(new Coding()
                    .setSystem(system)
                    .setCode(code.getTheCode())
                    .setDisplay(code.getTheDisplay())
            );
        return null;
    }

    public static CodeableConcept newCodeableConcept(Coding coding){
        return new CodeableConcept().addCoding(coding);
    }

    public static Extension newExtension(String profile, FhirCode value){
        return new Extension().setUrl(profile).setValue(new CodeType(value.getTheCode()));
    }

    public static Meta newMeta(String... profiles) {
        Meta meta = new Meta();
        Arrays.stream(profiles).forEach(meta::addProfile);
        return meta;
    }

    public static <R extends IBaseResource> Identifier newIdentifier(R resource){
        return newIdentifier(resource, resource.getIdElement().getValue());
    }

    public static <R extends IBaseResource> Identifier newIdentifier(R resource, String value){
        return newIdentifier(url(resource, dominio), value);
    }

    public static Identifier newIdentifier(String system, String value){
        return new Identifier().setSystem(system).setValue(value);
    }

    public static Reference newReference(String value){
        return new Reference(value);
    }

    public static Reference newReference(String system, String value){
        if(value != null) {
            String ref = system.concat(DELIMITER).concat(value);
            return newReference(ref);
        }
        return new Reference();
    }

    public static Reference newReferenceAsIdentifier(String system, String value, String display){
        return new Reference()
                .setIdentifier(newIdentifier(system, value))
                .setDisplay(display);
    }

    public static Reference newReferenceAsIdentifier(String system, String value){
        return newReferenceAsIdentifier(system, value, "");
    }

    public static Quantity newQuantity(String unit, Double value){
        return new Quantity().setUnit(unit).setValue(value);
    }

    public static Quantity newQuantity(String system, String code, String unit, Double value){
        return newQuantity(unit, value)
                .setSystem(system)
                .setCode(code);
    }

    public static ContactPoint newTelecom(String value, ContactPoint.ContactPointUse use){
        return newTelecom(value).setUse(use);
    }

    public static ContactPoint newTelecom(String value){
        return new ContactPoint()
                .setSystem(ContactPoint.ContactPointSystem.PHONE)
                .setValue(value);
    }

    private static <R extends IBaseResource> String url(R resource, String baseUrl){
        return baseUrl.concat(DELIMITER).concat(resource.fhirType());
    }

    private static String currentServletMapping(){
        return ServletUriComponentsBuilder.fromCurrentServletMapping().toUriString();
    }
}
