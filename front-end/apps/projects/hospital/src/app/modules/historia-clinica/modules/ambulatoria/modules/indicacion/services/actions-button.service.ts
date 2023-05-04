import { Injectable } from '@angular/core';
import { MatDialog, MatDialogRef } from '@angular/material/dialog';
import { DateDto, MasterDataInterface, ParenteralPlanDto, PharmacoSummaryDto } from '@api-rest/api-model';
import { HealthcareProfessionalService } from '@api-rest/services/healthcare-professional.service';
import { IndicationByProfessionalService } from '@api-rest/services/indication-by-professional.service';
import { InternmentIndicationService, OtherIndicationTypeDto } from '@api-rest/services/internment-indication.service';
import { InternmentStateService } from '@api-rest/services/internment-state.service';
import { BehaviorSubject } from 'rxjs';
import { INDICATION_TYPE } from '../constants/internment-indications';
import { MostFrequentComponent } from '../dialogs/most-frequent/most-frequent.component';
import { OtherIndicationComponent } from '../dialogs/other-indication/other-indication.component';
import { IndicationsFacadeService } from './indications-facade.service';
import { InternacionMasterDataService } from '@api-rest/services/internacion-master-data.service';
import { PharmacoComponent } from '../dialogs/pharmaco/pharmaco.component';
import { DietComponent } from '../dialogs/diet/diet.component';
import { Item } from '@presentation/pipes/paginate.pipe';
import { dateDtoToDate } from '@api-rest/mapper/date-dto.mapper';
import { ParenteralPlanComponent } from '../dialogs/parenteral-plan/parenteral-plan.component';

const DIALOG_SIZE = '45%';

@Injectable()

export class ActionsButtonService {
	INDICATION_TYPE = INDICATION_TYPE;
	ACTIVE_STATE = "Activo";
	patientId: number;
	actualDate = new Date;
	entryDate: Date;
	private professionalId: number;
	private othersIndicatiosType: OtherIndicationTypeDto[];
	private internmentEpisodeIdSubject = new BehaviorSubject<number>(null);
	private internmentEpisodeIdSubject$ = this.internmentEpisodeIdSubject.asObservable();
	private epicrisisConfirmedSubject = new BehaviorSubject<boolean>(false);
	private vias: MasterDataInterface<number>[] = [];
	private units: MasterDataInterface<number>[] = [];

	constructor(
		private readonly dialog: MatDialog,
		private readonly indicationsFacadeService: IndicationsFacadeService,
		private readonly healthcareProfessionalService: HealthcareProfessionalService,
		private readonly internmentIndicationService: InternmentIndicationService,
		private readonly internacionMasterdataService: InternacionMasterDataService,


	) {
		this.internacionMasterdataService.getVias().subscribe(v => this.vias = v);
		this.internacionMasterdataService.getUnits().subscribe(u => this.units = u);

		this.internmentEpisodeIdSubject$.subscribe(internmentEpisodeId => {
			if (internmentEpisodeId) {
				this.healthcareProfessionalService.getHealthcareProfessionalByUserId().subscribe((professionalId: number) =>
					this.professionalId = professionalId);

				this.indicationsFacadeService.setInternmentEpisodeId(internmentEpisodeId);

				this.internmentIndicationService.getOtherIndicationTypes().subscribe((othersIndicationsType: OtherIndicationTypeDto[]) => this.othersIndicatiosType = othersIndicationsType);

			}
		});

	}

	set internmentEpisodeId(internmentEpisodeId: number) {
		this.internmentEpisodeIdSubject.next(internmentEpisodeId);
	}

	set epicrisisConfirmed(epicrisisConfirmed: boolean) {
		this.epicrisisConfirmedSubject.next(epicrisisConfirmed);
	}

	get epicrisisConfirmed() {
		return this.epicrisisConfirmedSubject.value;
	}

	openDialog(indication: any, data?): MatDialogRef<any, any> {
		switch (indication) {
			case INDICATION_TYPE.DIET: {
				return this.openDietDialog();
			}
			case INDICATION_TYPE.OTHER_INDICATION: {
				return this.openOtherIndicationDialog();
			}
			case INDICATION_TYPE.PHARMACO: {
				return this.openPharmacoDialog(data);
			}
			case INDICATION_TYPE.PARENTERAL_PLAN: {
				return this.openMostFrequentParenteralPlansDialog(data);
			}
		}
	}


	openOtherIndicationDialog() {
		return this.dialog.open(OtherIndicationComponent, {
			disableClose: false,
			width: DIALOG_SIZE,
			height: '80%',
			data: {
				entryDate: this.entryDate,
				actualDate: this.actualDate,
				patientId: this.patientId,
				professionalId: this.professionalId,
				othersIndicatiosType: this.othersIndicatiosType
			}
		});
	}

	openPharmacoDialog(mostFrequent) {
		return this.dialog.open(MostFrequentComponent, {
			width: '50%',
			data: {
				items: mostFrequent,
				title: 'indicacion.card-pharmaco-frequent.TITLE'
			}
		});
	}

	openFormPharmacoDialog(pharmaco: PharmacoSummaryDto, diagnostics) {

		return this.dialog.open(PharmacoComponent, {
			data: {
				entryDate: this.entryDate,
				actualDate: this.actualDate,
				patientId: this.patientId,
				professionalId: this.professionalId,
				diagnostics,
				vias: this.vias,
				units: this.units,
				pharmaco
			},
			autoFocus: true,
			disableClose: false
		});
	}


	openDietDialog() {
		return this.dialog.open(DietComponent, {
			data: {
				entryDate: this.entryDate,
				actualDate: this.actualDate,
				patientId: this.patientId,
				professionalId: this.professionalId
			},
			disableClose: false,
			width: DIALOG_SIZE
		});

	}


	openParenteralPlanDialog(parenteralPlan?: ParenteralPlanDto) {
		return this.dialog.open(ParenteralPlanComponent, {
			data: {
				entryDate: this.entryDate,
				actualDate: this.actualDate,
				patientId: this.patientId,
				professionalId: this.professionalId,
				parenteralPlan
			},
			autoFocus: false,
			disableClose: true,
		});

	}

	openMostFrequentParenteralPlansDialog(parenteralPLanSpecialist) {
		return this.dialog.open(MostFrequentComponent,
			{
				width: '50%',
				data: {
					items: this.getParenteralPlansFrequent(parenteralPLanSpecialist),
					title: 'indicacion.most-frequent.TITLE_PARENTERAL'
				}
			});
	}


	getParenteralPlansFrequent(parenteralPLanSpecialist: ParenteralPlanDto[]): Item<ParenteralPlanDto>[] {

		let parenteralPlan: ParenteralPlanDto[];
		let mostFrequentParenteralPlan: Item<ParenteralPlanDto>[];

		this.indicationsFacadeService.parenteralPlans$.subscribe(p => parenteralPlan =
			p.filter((plan: ParenteralPlanDto) => this.getLastThreeDays(plan.indicationDate) && plan.professionalId === this.professionalId));

		const mostFrequent = parenteralPlan.concat(parenteralPLanSpecialist);

		mostFrequentParenteralPlan = mostFrequent.map((p: ParenteralPlanDto) => {
			return { description: p.snomed.pt, value: p, showDate: this.patientId === p.patientId }
		});

		return mostFrequentParenteralPlan;
	}

	private getLastThreeDays(valueDate: DateDto): boolean {
		const value = dateDtoToDate(valueDate);
		const today = new Date();
		const yesterday = new Date(today);
		yesterday.setDate(yesterday.getDate() - 1);
		const dayBeforeYesterday = new Date(yesterday);
		dayBeforeYesterday.setDate(dayBeforeYesterday.getDate() - 1);

		return (value.toDateString() === today.toDateString()) || (value.toDateString() === yesterday.toDateString())
			|| (value.toDateString() === dayBeforeYesterday.toDateString());
	}

}


interface ResultDialogPharmaco<T> {
	openDialogPharmacosFrequent: boolean;
	pharmaco?: T;
}
