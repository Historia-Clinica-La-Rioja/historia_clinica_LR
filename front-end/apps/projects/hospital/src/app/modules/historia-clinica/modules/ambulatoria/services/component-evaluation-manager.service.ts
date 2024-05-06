import { Injectable } from '@angular/core';
import { AllergyConditionDto, DiagnosisDto, EpicrisisDto, EpicrisisGeneralStateDto, ExternalCauseDto, HealthConditionDto, HealthHistoryConditionDto, HospitalizationProcedureDto, ImmunizationDto, MedicationDto, ResponseAnamnesisDto, ResponseEvolutionNoteDto } from '@api-rest/api-model';
import { BehaviorSubject, Observable } from 'rxjs';

@Injectable()
export class ComponentEvaluationManagerService {
	private allergiesSubject = new BehaviorSubject<boolean>(true);
	private familyHistoriesSubject = new BehaviorSubject<boolean>(true);
	private personalHistoriesSubject = new BehaviorSubject<boolean>(true);
	private vaccinesSubject = new BehaviorSubject<boolean>(true);
	private medicationsSubject = new BehaviorSubject<boolean>(true);
	private diagnosticosSubject = new BehaviorSubject<boolean>(true);
	private mainDiagnosticosSubject = new BehaviorSubject<boolean>(true);
	private hospitalizationProcedureSubject = new BehaviorSubject<boolean>(true);
	private otherProblemsSubject = new BehaviorSubject<boolean>(true);
	private externalCauseSubject = new BehaviorSubject<boolean>(true);

	set anamnesis(anamnesis: ResponseAnamnesisDto) {

		this.mainDiagnosis = anamnesis?.mainDiagnosis;
		this.diagnosis = anamnesis.diagnosis;
		this.hospitalizationProcedures = anamnesis?.procedures;
		this.allergies = anamnesis.allergies.content;
		this.familyHistories = anamnesis.familyHistories.content;
		this.personalHistories = anamnesis.personalHistories.content;
		this.vaccines = anamnesis.immunizations;
		this.medications = anamnesis.medications;
	}

	set evolutionNote(anamnesis: ResponseEvolutionNoteDto) {
		this.mainDiagnosis = anamnesis?.mainDiagnosis;
		this.diagnosis = anamnesis.diagnosis;
		this.hospitalizationProcedures = anamnesis?.procedures;
		this.allergies = anamnesis.allergies.content;
		this.vaccines = anamnesis.immunizations;
	}

	set epicrisis(epicrisis: EpicrisisGeneralStateDto) {
		this.allergies = epicrisis.allergies;
		this.diagnosis = epicrisis.diagnosis;
		this.familyHistories = epicrisis.familyHistories;
		this.vaccines = epicrisis.immunizations;
		this.mainDiagnosis = epicrisis.mainDiagnosis;
		this.medications = epicrisis.medications;
		this.otherProblems = epicrisis.otherProblems;
		this.personalHistories = epicrisis.personalHistories;
		this.hospitalizationProcedures = epicrisis?.procedures;
	}

	set epicrisisDraft(epicrisis: EpicrisisDto) {
		this.allergies = epicrisis.allergies;
		this.diagnosis = epicrisis.diagnosis;
		this.familyHistories = epicrisis.familyHistories;
		this.vaccines = epicrisis.immunizations;
		this.mainDiagnosis = epicrisis.mainDiagnosis;
		this.medications = epicrisis.medications;
		this.otherProblems = epicrisis.otherProblems;
		this.personalHistories = epicrisis.personalHistories;
		this.externalCause = epicrisis?.externalCause;
	}

	set mainDiagnosis(mainDiagnosis: DiagnosisDto) {
		this.mainDiagnosticosSubject.next(!mainDiagnosis);
	}

	set diagnosis(diagnosis: DiagnosisDto[]) {
		this.diagnosticosSubject.next(!(diagnosis?.length > 0));
	}

	set hospitalizationProcedures(hospitalizationProcedures: HospitalizationProcedureDto[]) {
		this.hospitalizationProcedureSubject.next(!(hospitalizationProcedures?.length > 0));
	}

	set allergies(allergies: AllergyConditionDto[]) {
		this.allergiesSubject.next(!allergies || allergies.length === 0);
	}

	set familyHistories(familyHistories: HealthHistoryConditionDto[]) {
		this.familyHistoriesSubject.next(!familyHistories || familyHistories.length === 0);
	}
	set personalHistories(personalHistories: HealthHistoryConditionDto[]) {
		this.personalHistoriesSubject.next(!personalHistories || personalHistories.length === 0);
	}
	set vaccines(vaccines: ImmunizationDto[]) {
		this.vaccinesSubject.next(!vaccines || vaccines.length === 0);
	}

	set procedures(mainDiagnosis: DiagnosisDto) {
		this.mainDiagnosticosSubject.next(!mainDiagnosis);
	}

	set otherProblems(otherProblems: HealthConditionDto[]) {
		this.otherProblemsSubject.next(!otherProblems || otherProblems.length === 0);
	}

	set medications(medications: MedicationDto[]) {
		this.medicationsSubject.next(!medications || medications.length === 0);
	}

	set externalCause(externalCause: ExternalCauseDto) {
		const { eventLocation, externalCauseType, snomed } = externalCause;
		const isExternalCauseEmpty = !(eventLocation || externalCauseType || snomed);
		this.externalCauseSubject.next(isExternalCauseEmpty);
	}

	isEmptyDiagnosis(): Observable<boolean> {
		return (this.mainDiagnosticosSubject.asObservable() || this.diagnosticosSubject.asObservable());
	}

	isEmptyAllergies(): Observable<boolean> {
		return this.allergiesSubject.asObservable();
	}

	isEmptyProcedure(): Observable<boolean> {
		return this.hospitalizationProcedureSubject.asObservable();
	}

	isEmptyFamilyHistories(): Observable<boolean> {
		return this.familyHistoriesSubject.asObservable();
	}

	isEmptyPersonalHistories(): Observable<boolean> {
		return this.personalHistoriesSubject.asObservable();
	}

	isEmptyVaccines(): Observable<boolean> {
		return this.vaccinesSubject.asObservable();
	}

	isEmptyMedications(): Observable<boolean> {
		return this.medicationsSubject.asObservable();
	}

	isOtherProblems(): Observable<boolean> {
		return this.otherProblemsSubject.asObservable();
	}

	isEmptyExternalCause(): Observable<boolean> {
		return this.externalCauseSubject.asObservable();
	}

}
