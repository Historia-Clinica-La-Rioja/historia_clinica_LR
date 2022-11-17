import { Component, Inject, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA, MatDialog } from '@angular/material/dialog';
import {
	CoverageDto,
	HealthInsuranceDto,
	MedicalCoverageDto,
	PrivateHealthInsuranceDto
} from '@api-rest/api-model';
import { HealthInsuranceService } from '@api-rest/services/health-insurance.service';
import { RenaperService } from '@api-rest/services/renaper.service';
import { DateFormat, momentFormat, momentParse, newMoment } from '@core/utils/moment.utils';
import { Moment } from 'moment';
import { EPatientMedicalCoverageCondition } from '@api-rest/api-model';
import { HealthInsuranceComponent } from "@pacientes/dialogs/health-insurance/health-insurance.component";
import { PrivateHealthInsuranceComponent } from "@pacientes/dialogs/private-health-insurance/private-health-insurance.component";
import { ArtComponent } from "@pacientes/dialogs/art/art.component";
import { map } from "rxjs/operators";
import { PatientMedicalCoverageService } from "@api-rest/services/patient-medical-coverage.service";
import { MapperService } from "@core/services/mapper.service";
import { pushIfNotExists } from '@core/utils/array.utils';

const DNI_TYPE_ID = 1;
@Component({
	selector: 'app-medical-coverage',
	templateUrl: './medical-coverage.component.html',
	styleUrls: ['./medical-coverage.component.scss']
})

export class MedicalCoverageComponent implements OnInit {

	patientMedicalCoverages: PatientMedicalCoverage[];
	loading = true;

	private healthInsuranceMasterData: MedicalCoverageDto[];

	constructor(
		private renaperService: RenaperService,
		public readonly healthInsuranceService: HealthInsuranceService,
		public dialogRef: MatDialogRef<MedicalCoverageComponent>,
		@Inject(MAT_DIALOG_DATA) public personInfo: {
			genderId: number;
			identificationNumber: string;
			identificationTypeId: number;
			patientId: number;
			initValues:  PatientMedicalCoverage[];
		},
		private readonly dialog: MatDialog,
		private readonly patientMedicalCoverageService: PatientMedicalCoverageService,
		private readonly mapperService: MapperService
	) {
		this.patientMedicalCoverages = this.personInfo.initValues ? this.personInfo.initValues : [];
	}


	ngOnInit(): void {
		this.healthInsuranceService.getAll().subscribe((values: MedicalCoverageDto[]) => {
			this.healthInsuranceMasterData = values;
			this.personInfo?.patientId ? this.setPatientMedicalCoverages() : this.patientMedicalCoverages = [];
			if (this.personInfo.identificationTypeId === DNI_TYPE_ID && this.personInfo.genderId) {
				this.renaperService.getHealthInsurance
					({ genderId: this.personInfo.genderId, identificationNumber: this.personInfo.identificationNumber })
					.subscribe((healthInsurances: MedicalCoverageDto[]) => {
						if (healthInsurances) {
							healthInsurances.forEach(healthInsurance => {
								const patientMedicalCoverage = this.patientMedicalCoverages
									.find(patientHealthInsurance => (patientHealthInsurance.medicalCoverage as HealthInsurance).rnos === healthInsurance.rnos);
								if (!patientMedicalCoverage) {
									this.patientMedicalCoverages = this.patientMedicalCoverages.concat(this.fromRenaperToPatientMedicalCoverage(healthInsurance));
								} else if (healthInsurance.dateQuery) {
									patientMedicalCoverage.validDate = momentParse(healthInsurance.dateQuery, DateFormat.YEAR_MONTH);
								}
							});
						}
						this.loading = false;
					}, _ => this.loading = false);
			} else {
				this.loading = false;
			}
		}
		);

	}

	private setPatientMedicalCoverages(): void {
		this.patientMedicalCoverageService.getActivePatientMedicalCoverages(this.personInfo.patientId)
			.pipe(
				map(
					patientMedicalCoveragesDto =>
						patientMedicalCoveragesDto.map(s => this.mapperService.toPatientMedicalCoverage(s))
				)
			)
			.subscribe((patientMedicalCoverages: PatientMedicalCoverage[]) =>
				patientMedicalCoverages.forEach(item=>{
				this.patientMedicalCoverages = pushIfNotExists<PatientMedicalCoverage>(this.patientMedicalCoverages, item, this.compareMedicalCorevage);
				})

			);
	}

	private compareMedicalCorevage(a1: PatientMedicalCoverage, a2: PatientMedicalCoverage): boolean {
		return a1.id === a2.id;
	}

	getFullHealthInsuranceText(healthInsurance: MedicalCoverageDto): string {
		return [healthInsurance.acronym, healthInsurance.name].filter(Boolean).join(' - ');
	}

	getMedicalCoveragePlanText(patientMedicalCoverage: PatientMedicalCoverage): string {
		return [patientMedicalCoverage?.planName?.toLowerCase(), patientMedicalCoverage?.condition?.toLowerCase()].filter(Boolean).join(' | ');
	}

	getDatesText(patientMedicalCoverage: PatientMedicalCoverage): string {
		const initText = patientMedicalCoverage.startDate ? 'desde ' +
			momentFormat(patientMedicalCoverage.startDate, DateFormat.VIEW_DATE) : '';

		const endText = patientMedicalCoverage.endDate ? ' hasta ' +
			momentFormat(patientMedicalCoverage.endDate, DateFormat.VIEW_DATE) : '';

		return initText + endText;
	}
	// -----------------------------------------------------------------------------------------------------------------------------

	save() {
		this.dialogRef.close({
			patientMedicalCoverages: this.patientMedicalCoverages.filter(pmc => pmc.id || isNewAndNotDeleted(pmc))
		});

		function isNewAndNotDeleted(pmc: PatientMedicalCoverage): boolean {
			return !pmc.id && pmc.active;
		}
	}

	close() {
		this.dialogRef.close();
	}

	getPatientHealthInsurances(): PatientMedicalCoverage[] {
		return this.patientMedicalCoverages.filter(s => s.medicalCoverage.type === EMedicalCoverageType.OBRASOCIAL && s.active);
	}

	getPatientPrivateHealthInsurances(): PatientMedicalCoverage[] {
		return this.patientMedicalCoverages.filter(s => s.medicalCoverage.type === EMedicalCoverageType.PREPAGA && s.active);
	}

	getPatientARTCoverages(): PatientMedicalCoverage[] {
		return this.patientMedicalCoverages.filter(s => s.medicalCoverage.type === EMedicalCoverageType.ART && s.active);
	}

	private fromRenaperToPatientMedicalCoverage(healthInsurance: MedicalCoverageDto): PatientMedicalCoverage {
		const healthInsuranceId = this.healthInsuranceMasterData
			.filter((s: MedicalCoverageDto) => s.rnos === healthInsurance.rnos)
			.map(s => s.id)[0];
		const medicalCoverage = new HealthInsurance(healthInsurance.rnos, healthInsurance.acronym,
			healthInsuranceId, healthInsurance.name, EMedicalCoverageType.OBRASOCIAL);

		if(medicalCoverage.id === undefined) {
			this.healthInsuranceService.get(parseInt(healthInsurance.rnos))
				.subscribe(data => {
					medicalCoverage.id = data.id;
				})
		}

		return {
			affiliateNumber: null,
			medicalCoverage,
			validDate: healthInsurance.dateQuery ? momentParse(healthInsurance.dateQuery, DateFormat.YEAR_MONTH) : newMoment(),
			active: true
		};
	}

	openAddHealthInsurance(healthInsurance?: PatientMedicalCoverage) {
		const dialogRef = this.dialog.open(HealthInsuranceComponent, {
			autoFocus: true,
			disableClose: true,
			data: {
				healthInsuranceMasterData: this.healthInsuranceMasterData,
				healthInsuranceToUpdate: healthInsurance
			}
		});
		dialogRef.afterClosed().subscribe( (healthInsurance:PatientMedicalCoverage) => {
			if (healthInsurance) {
				this.addMedicalCoverage(healthInsurance);
			}
		});
	}

	openAddPrivateHealthInsurance(privateHealthInsurance?: PatientMedicalCoverage) {
		const dialogRef = this.dialog.open(PrivateHealthInsuranceComponent, {
			autoFocus: true,
			disableClose: true,
			data: { privateHealthInsuranceToupdate: privateHealthInsurance }
		});
		dialogRef.afterClosed().subscribe( (privateHealthInsurance:PatientMedicalCoverage) => {
			if (privateHealthInsurance) {
				this.addMedicalCoverage(privateHealthInsurance);
			}
		});
	}

	openAddArt() {
		const dialogRef = this.dialog.open(ArtComponent, {
			autoFocus: true,
			disableClose: true
		});
		dialogRef.afterClosed().subscribe((art:PatientMedicalCoverage) => {
			if (art) {
				this.addMedicalCoverage(art);
			}
		});
	}

	private addMedicalCoverage(medicalCoverage: PatientMedicalCoverage) {
		const index =  this.patientMedicalCoverages.findIndex(patientMedicalCoverage => medicalCoverage.id ? patientMedicalCoverage.id == medicalCoverage.id :
			patientMedicalCoverage.medicalCoverage.name === medicalCoverage.medicalCoverage.name && patientMedicalCoverage.validDate === medicalCoverage.validDate)
		if (index != -1)
			this.patientMedicalCoverages.splice(index, 1, medicalCoverage);
		else
			this.patientMedicalCoverages = this.patientMedicalCoverages.concat(medicalCoverage);
	}

}

export interface PatientMedicalCoverage {
	id?: number;
	affiliateNumber?: string;
	validDate?: Moment;
	medicalCoverage: HealthInsurance | PrivateHealthInsurance | MedicalCoverage;
	startDate?: Moment;
	endDate?: Moment;
	planId?: number;
	planName?: string;
	active: boolean;
	condition?: EPatientMedicalCoverageCondition;
}
export class MedicalCoverage {
	id?: number;
	name: string;
	type: number;
	cuit: string;
	constructor(id, name, type, cuit) {
		this.id = id;
		this.name = name;
		this.type = type;
		this.cuit = cuit;
	}

	public toMedicalCoverageDto(): CoverageDto {
		return {
			id: this.id,
			name: this.name,
			cuit: this.cuit,
			type: EMedicalCoverageType.OBRASOCIAL,
		};
	}
}

export class HealthInsurance extends MedicalCoverage {
	rnos: string;
	acronym?: string;

	constructor(rnos: string, acronym: string, id, name, type) {
		super(id, name, type, null);
		this.rnos = rnos;
		this.acronym = acronym;
	}

	public toMedicalCoverageDto(): HealthInsuranceDto {
		return {
			id: this.id,
			acronym: this.acronym,
			name: this.name,
			cuit: this.cuit,
			rnos: Number(this.rnos),
			type: EMedicalCoverageType.OBRASOCIAL,
		};
	}
}

export class PrivateHealthInsurance extends MedicalCoverage {
	constructor(id, name, type, cuit) {
		super(id, name, type, cuit);
	}

	toMedicalCoverageDto(): PrivateHealthInsuranceDto {
		return {
			id: this.id,
			name: this.name,
			cuit: this.cuit,
			type: EMedicalCoverageType.PREPAGA
		};
	}
}

export enum EMedicalCoverageType {
	PREPAGA = 1, OBRASOCIAL, ART
	}

export function determineIfIsHealthInsurance(toBeDetermined: HealthInsurance | PrivateHealthInsurance): toBeDetermined is HealthInsurance {
	if ((toBeDetermined as HealthInsurance).type) {
		return true;
	}
	return false; // case PrivateHealthInsurance
}
