package net.pladema.patient.service;

import static net.pladema.patient.service.StringHelper.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import net.pladema.patient.controller.dto.PatientSearchFilter;
import net.pladema.patient.service.domain.MatchCalculationException;
import net.pladema.person.repository.entity.Person;

public class MathScore {

	private enum SearchField {

		LastName(0.2f),
		FirstName(0.1f),
		IdentificationType(0.1f),
		IdentificationNumber(0.3f),
		Gender(0.1f),
		BirthDate(0.2f);
		
		private final float coefficent;
		
		SearchField(float coefficent){
			this.coefficent = coefficent;
		}
		
		public float getCoefficent() {
			return coefficent;
		}
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
		partialResult += calculateMatchScore(soundex(searchFilter.getFirstName()), soundex(personToMatch.getFirstName()),
				SearchField.FirstName.getCoefficent());
		partialResult += calculateMatchScore(soundex(searchFilter.getLastName()), soundex(personToMatch.getLastName()),
				SearchField.LastName.getCoefficent());
		partialResult += calculateMatchScore(searchFilter.getIdentificationNumber(), personToMatch.getIdentificationNumber(),
				SearchField.IdentificationNumber.getCoefficent());
		partialResult += calculateMatchScore(formatDate(searchFilter.getBirthDate()),
				formatDate(personToMatch.getBirthDate()), SearchField.BirthDate.getCoefficent());
		return partialResult;
	}

	private static float sumFullMatchCases(PatientSearchFilter searchFilter, Person personToMatch,
			Float partialResult) {
		partialResult += assertNulls(personToMatch.getIdentificationTypeId(), searchFilter.getIdentificationTypeId())
				&& personToMatch.getIdentificationTypeId().equals(searchFilter.getIdentificationTypeId())
				? SearchField.IdentificationType.getCoefficent()
				: 0;
		partialResult += assertNulls(searchFilter.getGenderId(), personToMatch.getGenderId())
				&& personToMatch.getGenderId().equals(searchFilter.getGenderId()) ? SearchField.Gender.getCoefficent()
				: 0;
		return partialResult;
	}

	private static String formatDate(LocalDate date) {
		return date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
	}
	
	private static boolean assertNulls(Object filterAtribute, Object atributeToMatchh) {
		return filterAtribute != null && atributeToMatchh != null;
	}

	public static float calculateMatchScore(String obtainedText, String searchedText, float coefficient) {
		if (isNullOrWhiteSpace(obtainedText)) {
			return 0;
		}
		if (isNullOrWhiteSpace(searchedText)) {
			return 0;
		}

		float ret = 0;
		int strLen = obtainedText.length();
		int distance = computeLevenshteinDistance(obtainedText, searchedText);

		if (noMatch(strLen, distance))
			return 0;

		if (totalMatch(distance))
			return coefficient;

		if (partialMatch(strLen, distance)) {
			ret = calculateMatchValue(coefficient, strLen, distance);
		}

		return ret;
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
