import { Injectable } from '@angular/core';
import { HceGeneralStateService } from '@api-rest/services/hce-general-state.service';
import { BehaviorSubject, Subject } from 'rxjs';

@Injectable({
	providedIn: 'root'
})
export class AmbulatoriaSummaryFacadeService {

	private idPaciente: number;

	private allergiesSubject: Subject<any> = new BehaviorSubject<any>([]);
	private familyHistoriesSubject: Subject<any> = new BehaviorSubject<any>([]);
	private personalHistoriesSubject: Subject<any> = new BehaviorSubject<any>([]);
	private medicationsSubject: Subject<any> = new BehaviorSubject<any>([]);
	private vitalSignsSubject: Subject<any> = new BehaviorSubject<any>([]);
	private anthropometricDataSubject: Subject<any> = new BehaviorSubject<any>([]);

	public readonly allergies$ = this.allergiesSubject.asObservable();
	public readonly familyHistories$ = this.familyHistoriesSubject.asObservable();
	public readonly personalHistories$ = this.personalHistoriesSubject.asObservable();
	public readonly medications$ = this.medicationsSubject.asObservable();
	public readonly vitalSigns$ = this.vitalSignsSubject.asObservable();
	public readonly anthropometricData$ = this.anthropometricDataSubject.asObservable();


	constructor(private readonly hceGeneralStateService: HceGeneralStateService) {
	}

	setIdPaciente(idPaciente: number) {
		this.idPaciente = idPaciente;
		this.setFieldsToUpdate({
			allergies: true,
			familyHistories: true,
			personalHistories: true,
			vitalSigns: true,
			medications: true,
			anthropometricData: true,
		});
	}

	setFieldsToUpdate(fieldsToUpdate: AmbulatoriaFields): void {
		if (fieldsToUpdate.allergies) {
			this.hceGeneralStateService.getAllergies(this.idPaciente).subscribe(a => this.allergiesSubject.next(a));
		}

		if (fieldsToUpdate.familyHistories) {
			this.hceGeneralStateService.getFamilyHistories(this.idPaciente).subscribe(fH => this.familyHistoriesSubject.next(fH));
		}

		if (fieldsToUpdate.personalHistories) {
			this.hceGeneralStateService.getPersonalHistories(this.idPaciente).subscribe(ph => this.personalHistoriesSubject.next(ph));
		}

		if (fieldsToUpdate.vitalSigns) {
			this.hceGeneralStateService.getVitalSigns(this.idPaciente).subscribe(vs => this.vitalSignsSubject.next(vs));
		}

		if (fieldsToUpdate.medications) {
			this.hceGeneralStateService.getMedications(this.idPaciente).subscribe(m => this.medicationsSubject.next(m));
		}

		if (fieldsToUpdate.anthropometricData) {
			this.hceGeneralStateService.getAnthropometricData(this.idPaciente).subscribe(aD => this.anthropometricDataSubject.next(aD));
		}
	}
}

export interface AmbulatoriaFields {
	allergies?: boolean;
	familyHistories?: boolean;
	personalHistories?: boolean;
	vitalSigns?: boolean;
	medications?: boolean;
	anthropometricData?: boolean;
	problems?: boolean;
	procedures?: boolean;
	reasons?: boolean;
}
