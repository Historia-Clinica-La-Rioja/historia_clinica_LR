package net.pladema.snowstorm.services;

import net.pladema.snowstorm.services.domain.Cie10RuleFeature;

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

    public static boolean check(String rule, Cie10RuleFeature features) {
        switch (rule) {
            case IF_FEMALE:
                return evaluateGender(features, Cie10RuleFeature.FEMALE);

            case IF_MALE:
                return evaluateGender(features, Cie10RuleFeature.MALE);

            case IF_CURRENT_AGE_LESS_OR_EQUAL_15_YEARS:
                return evaluateCurrentAgeLessOrEqual(features, 15);

            case IF_CURRENT_AGE_LESS_OR_EQUAL_18_YEARS:
                return evaluateCurrentAgeLessOrEqual(features, 18);

            case IF_CURRENT_AGE_GREATER_OR_EQUAL_65_YEARS:
                return evaluateCurrentAgeGreaterOrEqual(features, 65);

            case IF_AGE_ONSET_LESS_OR_EQUAL_15_YEARS:
                return evaluateAgeOnsetLessOrEqual(features, 15);

            case IF_AGE_ONSET_LESS_19_YEARS:
                return evaluateAgeOnsetLess(features, 19);

            case IF_AGE_ONSET_GREATER_OR_EQUAL_12_AND_LESS_19_YEARS:
                return evaluateAgeOnsetGraterOrEqualAndLess(features, 12, 19);

            case TRUE:
                return true;

            case OTHERWISE_TRUE:
                return true;

            default:
                return false;
        }
    }

    private static boolean evaluateGender(Cie10RuleFeature features, short male) {
        return Short.valueOf(male).equals(getPatientGender(features));
    }

    private static boolean evaluateAgeOnsetGraterOrEqualAndLess(Cie10RuleFeature features, int i, int j) {
        Short ageOnset = getPatientAgeOnset(features);
        return ageOnset != null && (ageOnset >= i) && (ageOnset < j);
    }

    private static boolean evaluateAgeOnsetLess(Cie10RuleFeature features, int i) {
        Short ageOnset = getPatientAgeOnset(features);
        return ageOnset != null && (ageOnset < i);
    }

    private static boolean evaluateAgeOnsetLessOrEqual(Cie10RuleFeature features, int i) {
        Short ageOnset = getPatientAgeOnset(features);
        return ageOnset != null && (ageOnset <= i);
    }

    private static boolean evaluateCurrentAgeGreaterOrEqual(Cie10RuleFeature features, int i) {
        Short age = getPatientAge(features);
        return age != null && (age >= i);
    }

    private static boolean evaluateCurrentAgeLessOrEqual(Cie10RuleFeature features, int i) {
        Short age = getPatientAge(features);
        return age != null && (age <= i);
    }

    private static Short getPatientAgeOnset(Cie10RuleFeature features) {
        return features.getAge();
    }

    private static Short getPatientGender(Cie10RuleFeature features) {
        return features.getGender();
    }

    private static Short getPatientAge(Cie10RuleFeature features) {
        return features.getAge();
    }

}
