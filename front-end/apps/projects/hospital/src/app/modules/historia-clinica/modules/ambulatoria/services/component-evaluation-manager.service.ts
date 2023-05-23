import { Injectable } from '@angular/core';
import { AllergyConditionDto, DiagnosisDto, HealthHistoryConditionDto, ImmunizationDto, MedicationDto, ResponseAnamnesisDto } from '@api-rest/api-model';
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
	set anamnesis(anamnesis: ResponseAnamnesisDto) {

		this.mainDiagnosis = anamnesis?.mainDiagnosis;
		this.diagnosis = anamnesis.diagnosis;
		this.allergies = anamnesis.allergies;
		this.familyHistories = anamnesis.familyHistories;
		this.personalHistories = anamnesis.personalHistories;
		this.vaccines = anamnesis.immunizations;
		this.medications = anamnesis.medications;
	}


	set mainDiagnosis(mainDiagnosis: DiagnosisDto) {
		this.mainDiagnosticosSubject.next(!mainDiagnosis);
	}

	set diagnosis(diagnosis: DiagnosisDto[]) {
		this.diagnosticosSubject.next(!(diagnosis?.length > 0));
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

	set medications(medications: MedicationDto[]) {

		this.medicationsSubject.next(!medications || medications.length === 0);
	}

	isEmptyDiagnosis(): Observable<boolean> {
		return (this.mainDiagnosticosSubject.asObservable() || this.diagnosticosSubject.asObservable());
	}

	isEmptyAllergies(): Observable<boolean> {
		return this.allergiesSubject.asObservable();
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

}
