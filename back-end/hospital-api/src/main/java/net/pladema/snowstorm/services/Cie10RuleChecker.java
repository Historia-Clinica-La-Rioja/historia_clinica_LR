package net.pladema.snowstorm.services;

import net.pladema.clinichistory.documents.service.domain.PatientInfoBo;
import net.pladema.person.repository.entity.Gender;

public class Cie10RuleChecker {

    private static final String IF_FEMALE = "IFA 248152002 | Female (finding) |";
    private static final String IF_MALE = "IFA 248153007 | Male (finding) |";
    private static final String IF_CURRENT_AGE_LESS_OR_EQUAL_15_YEARS = "IFA 424144002 | Current chronological age (observable entity) | <= 15.0 years";
    private static final String IF_CURRENT_AGE_LESS_OR_EQUAL_18_YEARS = "IFA 424144002 | Current chronological age (observable entity) | <= 18.0 years";
    private static final String IF_CURRENT_AGE_GREATER_OR_EQUAL_65_YEARS = "IFA 424144002 | Current chronological age (observable entity) | >= 65.0 years";
    private static final String IF_AGE_ONSET_LESS_OR_EQUAL_15_YEARS = "IFA 445518008 | Age at onset of clinical finding (observable entity) | <= 15.0 years";
    private static final String IF_AGE_ONSET_LESS_19_YEARS = "IFA 445518008 | Age at onset of clinical finding (observable entity) | < 19.0 years";
    private static final String IF_AGE_ONSET_GREATER_OR_EQUAL_12_AND_LESS_19_YEARS = "IFA 445518008 | Age at onset of clinical finding (observable entity) | >= 12.0 years AND IFA 445518008 | Age at onset of clinical finding (observable entity) | < 19.0 years";
    private static final String TRUE = "TRUE";
    private static final String OTHERWISE_TRUE = "OTHERWISE TRUE";

    public static boolean check(String rule, PatientInfoBo patient) {
        switch (rule) {
            case IF_FEMALE:
                return evaluateGender(patient, Gender.FEMALE);

            case IF_MALE:
                return evaluateGender(patient, Gender.MALE);

            case IF_CURRENT_AGE_LESS_OR_EQUAL_15_YEARS:
                return evaluateCurrentAgeLessOrEqual(patient, 15);

            case IF_CURRENT_AGE_LESS_OR_EQUAL_18_YEARS:
                return evaluateCurrentAgeLessOrEqual(patient, 18);

            case IF_CURRENT_AGE_GREATER_OR_EQUAL_65_YEARS:
                return evaluateCurrentAgeGreaterOrEqual(patient, 65);

            case IF_AGE_ONSET_LESS_OR_EQUAL_15_YEARS:
                return evaluateAgeOnsetLessOrEqual(patient, 15);

            case IF_AGE_ONSET_LESS_19_YEARS:
                return evaluateAgeOnsetLess(patient, 19);

            case IF_AGE_ONSET_GREATER_OR_EQUAL_12_AND_LESS_19_YEARS:
                return evaluateAgeOnsetGraterOrEqualAndLess(patient, 12, 19);

            case TRUE:
                return true;

            case OTHERWISE_TRUE:
                return true;

            default:
                return false;
        }
    }

    private static boolean evaluateGender(PatientInfoBo patient, short male) {
        return Short.valueOf(male).equals(getPatientGender(patient));
    }

    private static boolean evaluateAgeOnsetGraterOrEqualAndLess(PatientInfoBo patient, int i, int j) {
        Short ageOnset = getPatientAgeOnset(patient);
        return ageOnset != null && (ageOnset >= i) && (ageOnset < j);
    }

    private static boolean evaluateAgeOnsetLess(PatientInfoBo patient, int i) {
        Short ageOnset = getPatientAgeOnset(patient);
        return ageOnset != null && (ageOnset < i);
    }

    private static boolean evaluateAgeOnsetLessOrEqual(PatientInfoBo patient, int i) {
        Short ageOnset = getPatientAgeOnset(patient);
        return ageOnset != null && (ageOnset <= i);
    }

    private static boolean evaluateCurrentAgeGreaterOrEqual(PatientInfoBo patient, int i) {
        Short age = getPatientAge(patient);
        return age != null && (age >= i);
    }

    private static boolean evaluateCurrentAgeLessOrEqual(PatientInfoBo patient, int i) {
        Short age = getPatientAge(patient);
        return age != null && (age <= i);
    }

    private static Short getPatientAgeOnset(PatientInfoBo patient) {
        return patient.getAge();
    }

    private static Short getPatientGender(PatientInfoBo patient) {
        return patient.getGenderId();
    }

    private static Short getPatientAge(PatientInfoBo patient) {
        return patient.getAge();
    }

}
