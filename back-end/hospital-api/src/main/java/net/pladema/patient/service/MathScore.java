package net.pladema.patient.service;

import static ar.lamansys.sgx.shared.strings.StringHelper.isNullOrWhiteSpace;
import static ar.lamansys.sgx.shared.strings.StringHelper.soundex;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

import net.pladema.patient.controller.dto.PatientSearchFilter;
import net.pladema.patient.service.domain.MatchCalculationException;
import net.pladema.person.repository.entity.Person;

public class MathScore {

	private enum SearchField {

		LAST_NAME(0.22f, 0.13f, false),
		FIRST_NAME(0.08f, 0.04f, false),
		IDENTIFICATION_NUMBER(0.39f, 0.26f, true),
		GENDER(0.05f, 0.05f, false),
		BIRTH_DATE(0.26f, 0.17f, false);
		
		private final float coefficient;
		private final float minimum;
		private final boolean partial;
		
		SearchField(float coefficient, float minimum, boolean partial){
			this.coefficient = coefficient;
			this.minimum = minimum;
			this.partial = partial;
		}
		
		public float getCoefficient() {
			return coefficient;
		}

		public float getMinimum() {
			return minimum;
		}

		public boolean isPartial() {
			return partial;
		}
	}

	public static float calculateMatchWithProvidedAttributes(PatientSearchFilter searchFilter, Person personToMatch){
		float multiplier = calculateScoreMultiplier(searchFilter);
		return calculateMatchAdjustedToMultiplier(searchFilter, personToMatch, multiplier);
	}

	public static float calculateMatchAdjustedToMultiplier(PatientSearchFilter searchFilter, Person personToMatch, float multiplier){
		float result = calculateMatch(searchFilter, personToMatch) * multiplier;
		return Math.min(result, 100f); // the result may exceed 100 because of the previous multiplication, so it needs to be adjusted
	}

	public static float calculateScoreMultiplier(PatientSearchFilter searchFilter) {
		float divider = 0f;
		if ((searchFilter.getFirstName() != null) || (searchFilter.getMiddleNames() != null))
			divider += SearchField.FIRST_NAME.getCoefficient();
		if ((searchFilter.getLastName() != null) || (searchFilter.getOtherLastNames() != null))
			divider += SearchField.LAST_NAME.getCoefficient();
		if (searchFilter.getIdentificationNumber() != null)
			divider += SearchField.IDENTIFICATION_NUMBER.getCoefficient();
		if (searchFilter.getGenderId() != null)
			divider += SearchField.GENDER.getCoefficient();
		if (searchFilter.getBirthDate() != null)
			divider += SearchField.BIRTH_DATE.getCoefficient();
		return (divider != 0) ? (1 / divider) : 0;
	}

	public static float calculateMatch(PatientSearchFilter searchFilter, Person personToMatch){
		Float partialResult = 0f;
		partialResult = sumFullMatchCases(searchFilter, personToMatch, partialResult);
		partialResult = sumPartialMatchCases(searchFilter, personToMatch, partialResult);
		if (partialResult>1) {
			throw new MatchCalculationException();
		}
		return partialResult * 100;
	}
	
	private static Float sumPartialMatchCases(PatientSearchFilter searchFilter, Person personToMatch, Float partialResult) {
		String filterNameComplete = buildCompleteName(searchFilter.getFirstName(), searchFilter.getMiddleNames());
		String filterLastNameComplete = buildCompleteName(searchFilter.getLastName(), searchFilter.getOtherLastNames());
		String personNameComplete = buildCompleteName(personToMatch.getFirstName(), personToMatch.getMiddleNames());
		String personLastNameComplete = buildCompleteName(personToMatch.getLastName(), personToMatch.getOtherLastNames());

		partialResult += hasContent(filterNameComplete, personNameComplete) ?
				calculateMatchScore(soundex(filterNameComplete), soundex(personNameComplete),
				SearchField.FIRST_NAME) : 0;
		partialResult += hasContent(filterLastNameComplete, personLastNameComplete) ?
				calculateMatchScore(soundex(filterLastNameComplete), soundex(personLastNameComplete),
				SearchField.LAST_NAME) : 0;
		partialResult += assertNulls(personToMatch.getIdentificationNumber(), searchFilter.getIdentificationNumber()) ?
				calculateMatchScore(searchFilter.getIdentificationNumber(), personToMatch.getIdentificationNumber(),
				SearchField.IDENTIFICATION_NUMBER) : 0;
		return partialResult;
	}

	private static boolean hasContent(String filterNameComplete, String personNameComplete) {
		return !filterNameComplete.isBlank() && !personNameComplete.isBlank();
	}

	private static String buildCompleteName(String ...names) {
		return Arrays.asList(names).stream().filter(Objects::nonNull).collect(Collectors.joining(" "));
	}
	
	private static float sumFullMatchCases(PatientSearchFilter searchFilter, Person personToMatch,
			Float partialResult) {
		partialResult += assertNulls(searchFilter.getGenderId(), personToMatch.getGenderId())
				&& personToMatch.getGenderId().equals(searchFilter.getGenderId()) ? SearchField.GENDER.getCoefficient()
				: 0;
		partialResult += assertNulls(searchFilter.getBirthDate(), personToMatch.getBirthDate())
				&& formatDate(personToMatch.getBirthDate()).equals(formatDate(searchFilter.getBirthDate())) ?
						SearchField.BIRTH_DATE.getCoefficient() : 0;
		return partialResult;
	}

	private static String formatDate(LocalDate date) {
		return date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
	}
	
	private static boolean assertNulls(Object... filterAttributes) {
		return Arrays.asList(filterAttributes).stream().noneMatch(Objects::isNull);
	}

	public static float calculateMatchScore(String obtainedText, String searchedText, SearchField field) {
		if (isNullOrWhiteSpace(obtainedText)) {
			return 0;
		}
		if (isNullOrWhiteSpace(searchedText)) {
			return 0;
		}

		int strLen = obtainedText.length();
		int distance = computeLevenshteinDistance(obtainedText, searchedText);

		float coefficient = field.getCoefficient();
		if (noMatch(strLen, distance))
			return 0;

		if (totalMatch(distance))
			return coefficient;

		if(!field.isPartial())
			return distance <= 2 ? field.getMinimum() : 0;
		else {
			float ret = calculateMatchValue(coefficient, strLen, distance);
			return ret < field.getMinimum() ? 0 : ret;
		}
	}

	private static float calculateMatchValue(float coefficient, int strLen, int distance) {
		float ret;
		float matchCoef = ((float) (strLen - distance) / (float) strLen);
		ret = (matchCoef * coefficient);
		return ret;
	}

	private static boolean partialMatch(int strLen, int distance) {
		return distance < strLen;
	}

	private static boolean totalMatch(int distance) {
		return distance == 0;
	}

	private static boolean noMatch(int strLen, int distance) {
		return distance >= strLen;
	}

//Distancia de Levenshtein: número mínimo de operaciones requeridas para transformar una cadena de caracteres en otra
	public static int computeLevenshteinDistance(String str1, String str2) {
		return computeLevenshteinDistance(str1.toCharArray(), str2.toCharArray());
	}

	private static int computeLevenshteinDistance(char[] str1, char[] str2) {
		int[][] distance = new int[str1.length + 1][str2.length + 1];

		for (int i = 0; i <= str1.length; i++) {
			distance[i][0] = i;
		}
		for (int j = 0; j <= str2.length; j++) {
			distance[0][j] = j;
		}
		for (int i = 1; i <= str1.length; i++) {
			for (int j = 1; j <= str2.length; j++) {
				int cost = (str1[i - 1] == str2[j - 1]) ? 0 : 1;
				distance[i][j] = minimum(distance[i - 1][j] + 1, distance[i][j - 1] + 1, distance[i - 1][j - 1] + cost);
			}
		}
		return distance[str1.length][str2.length];
	}

	private static int minimum(int a, int b, int c) {
		return Math.min(a, Math.min(b, c));
	}

}
