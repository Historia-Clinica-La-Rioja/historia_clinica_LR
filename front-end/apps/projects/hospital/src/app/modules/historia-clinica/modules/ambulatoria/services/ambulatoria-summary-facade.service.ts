import { Injectable } from '@angular/core';
import { HceGeneralStateService } from '@api-rest/services/hce-general-state.service';
import { BehaviorSubject, Subject } from 'rxjs';
import { HistoricalProblemsFacadeService } from './historical-problems-facade.service';
import { AppointmentsService } from '@api-rest/services/appointments.service';
import { HCEAnthropometricDataDto } from '@api-rest/api-model';

@Injectable({providedIn: 'root'})
export class AmbulatoriaSummaryFacadeService {

	private idPaciente: number;

	private allergiesSubject: Subject<any> = new BehaviorSubject<any>([]);
	private personalHistoriesSubject: Subject<any> = new BehaviorSubject<any>([]);
	private familyHistoriesSubject: Subject<any> = new BehaviorSubject<any>([]);
	private patientProblemsSubject: Subject<any> = new BehaviorSubject<any>([]);
	private patientProblemsByRoleSubject: Subject<any> = new BehaviorSubject<any>([]);
	private medicationsSubject: Subject<any> = new BehaviorSubject<any>([]);
	private riskFactorsSubject: Subject<any> = new BehaviorSubject<any>([]);
	private bloodTypeSubject: Subject<string> = new BehaviorSubject<string>(null);
	private anthropometricDataListSubject: Subject<HCEAnthropometricDataDto[]> = new BehaviorSubject<HCEAnthropometricDataDto[]>([]);
	private activeProblemsSubject: Subject<any> = new BehaviorSubject<any>([]);
	private chronicProblemsSubject: Subject<any> = new BehaviorSubject<any>([]);
	private solvedProblemsSubject: Subject<any> = new BehaviorSubject<any>([]);
	private amendedProblemsSubject: Subject<any> = new BehaviorSubject<any>([]);
	private hasNewConsultationEnabledSubject: Subject<any> = new BehaviorSubject<boolean>(false);
	private isNewConsultationOpenSubject: Subject<any> = new BehaviorSubject<boolean>(false);
	updateReferences: Subject<boolean> = new BehaviorSubject<boolean>(false);

	public readonly allergies$ = this.allergiesSubject.asObservable();
	public readonly personalHistories$ = this.personalHistoriesSubject.asObservable();
	public readonly familyHistories$ = this.familyHistoriesSubject.asObservable();
	public readonly patientProblems$ = this.patientProblemsSubject.asObservable();
	public readonly patientProblemsByRole$ = this.patientProblemsByRoleSubject.asObservable();
	public readonly medications$ = this.medicationsSubject.asObservable();
	public readonly riskFactors$ = this.riskFactorsSubject.asObservable();
	public readonly bloodType$ = this.bloodTypeSubject.asObservable();
	public readonly anthropometricDataList$ = this.anthropometricDataListSubject.asObservable();
	public readonly activeProblems$ = this.activeProblemsSubject.asObservable();
	public readonly chronicProblems$ = this.chronicProblemsSubject.asObservable();
	public readonly solvedProblems$ = this.solvedProblemsSubject.asObservable();
	public readonly amendedProblems$ = this.amendedProblemsSubject.asObservable();
	public readonly hasNewConsultationEnabled$ = this.hasNewConsultationEnabledSubject.asObservable();
	public readonly isNewConsultationOpen$ = this.isNewConsultationOpenSubject.asObservable();

	constructor(
		private readonly hceGeneralStateService: HceGeneralStateService,
		private readonly appointmentsService: AppointmentsService,
		private readonly historialProblemsFacadeService: HistoricalProblemsFacadeService) {
	}

	setIdPaciente(idPaciente: number) {
		this.idPaciente = idPaciente;
		this.setFieldsToUpdate({
			allergies: true,
			personalHistories: true,
			familyHistories: true,
			patientProblems: true,
			patientProblemsByRole: true,
			riskFactors: true,
			medications: true,
			anthropometricData: true,
			problems: true,
			references: true
		});
	}

	setFieldsToUpdate(fieldsToUpdate: AmbulatoriaFields): void {
		if (fieldsToUpdate.allergies) {
			this.hceGeneralStateService.getAllergies(this.idPaciente).subscribe(a => this.allergiesSubject.next(a));
		}

		if (fieldsToUpdate.personalHistories) {
			this.hceGeneralStateService.getPersonalHistories(this.idPaciente).subscribe(pH => this.personalHistoriesSubject.next(pH));
		}

		if (fieldsToUpdate.familyHistories) {
			this.hceGeneralStateService.getFamilyHistories(this.idPaciente).subscribe(fH => this.familyHistoriesSubject.next(fH));
		}

		if (fieldsToUpdate.patientProblems) {
			this.hceGeneralStateService.getPatientProblems(this.idPaciente).subscribe(ph => this.patientProblemsSubject.next(ph));
		}
		if (fieldsToUpdate.patientProblemsByRole) {
			this.hceGeneralStateService.getPatientProblemsByRole(this.idPaciente).subscribe(ph => this.patientProblemsByRoleSubject.next(ph));
		}

		if (fieldsToUpdate.riskFactors) {
			this.hceGeneralStateService.getRiskFactors(this.idPaciente).subscribe(vs => this.riskFactorsSubject.next(vs));
		}

		if (fieldsToUpdate.medications) {
			this.hceGeneralStateService.getMedications(this.idPaciente).subscribe(m => this.medicationsSubject.next(m));
		}

		if (fieldsToUpdate.anthropometricData) {
			this.hceGeneralStateService.getLast2AnthropometricData(this.idPaciente).subscribe(
				(aD: HCEAnthropometricDataDto[]) => {
					this.bloodTypeSubject.next(aD[0]?.bloodType?.value);
					this.anthropometricDataListSubject.next(aD);
				}
			);
		}

		if (fieldsToUpdate.problems) {
			this.hceGeneralStateService.getActiveProblems(this.idPaciente).subscribe(p => {
				this.activeProblemsSubject.next(p);
			});
			this.hceGeneralStateService.getChronicConditions(this.idPaciente).subscribe(c => {
				this.chronicProblemsSubject.next(c);
			});
			this.hceGeneralStateService.getSolvedProblems(this.idPaciente).subscribe(c => {
				this.solvedProblemsSubject.next(c);
			});
			this.hceGeneralStateService.getAmendedProblems(this.idPaciente).subscribe(c => {
				this.amendedProblemsSubject.next(c);
			});
		}
		this.historialProblemsFacadeService.loadEvolutionSummaryList(this.idPaciente);
		this.appointmentsService.hasNewConsultationEnabled(this.idPaciente)
			.subscribe(h => this.hasNewConsultationEnabledSubject.next(h));

		if (fieldsToUpdate.references) {
			this.updateReferences.next(true)
		}
	}

	setIsNewConsultationOpen(isNewConsultationOpen: boolean){
		this.isNewConsultationOpenSubject.next(isNewConsultationOpen);
	}
}

export interface AmbulatoriaFields {
	allergies?: boolean;
	personalHistories?: boolean;
	familyHistories?: boolean;
	patientProblems?: boolean;
	patientProblemsByRole?: boolean;
	riskFactors?: boolean;
	medications?: boolean;
	anthropometricData?: boolean;
	problems?: boolean;
	references?: boolean;
}
