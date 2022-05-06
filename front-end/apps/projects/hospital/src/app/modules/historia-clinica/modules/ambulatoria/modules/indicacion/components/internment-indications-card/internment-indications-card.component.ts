import { Component, Input, OnInit } from '@angular/core';
import { INTERNMENT_INDICATIONS } from "@historia-clinica/constants/summaries";
import { getDay, getMonth, isTomorrow, isYesterday, isToday, differenceInCalendarDays, isSameDay } from "date-fns";
import { MONTHS_OF_YEAR, DAYS_OF_WEEK } from "@historia-clinica/modules/ambulatoria/modules/indicacion/constants/internment-indications";
import { InternmentEpisodeService } from "@api-rest/services/internment-episode.service";
import { DiagnosesGeneralStateDto, DietDto, MasterDataInterface, OtherIndicationDto, ParenteralPlanDto, PharmacoDto } from "@api-rest/api-model";
import { DateTimeDto } from "@api-rest/api-model";
import { DietComponent } from '../../dialogs/diet/diet.component';
import { MatDialog } from '@angular/material/dialog';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { HealthcareProfessionalService } from '@api-rest/services/healthcare-professional.service';
import { IndicationsFacadeService } from "@historia-clinica/modules/ambulatoria/modules/indicacion/services/indications-facade.service";
import { dateDtoToDate } from "@api-rest/mapper/date-dto.mapper";
import { OtherIndicationComponent } from '../../dialogs/other-indication/other-indication.component';
import { InternmentIndicationService, OtherIndicationTypeDto } from '@api-rest/services/internment-indication.service';
import { ParenteralPlanComponent } from "@historia-clinica/modules/ambulatoria/modules/indicacion/dialogs/parenteral-plan/parenteral-plan.component";
import { PharmacoComponent } from '../../dialogs/pharmaco/pharmaco.component';
import { InternmentStateService } from '@api-rest/services/internment-state.service';
import { InternacionMasterDataService } from '@api-rest/services/internacion-master-data.service';

const DIALOG_SIZE = '45%';

@Component({
	selector: 'app-internment-indications-card',
	templateUrl: './internment-indications-card.component.html',
	styleUrls: ['./internment-indications-card.component.scss']
})
export class InternmentIndicationsCardComponent implements OnInit {

	internmentIndication = INTERNMENT_INDICATIONS;
	viewDay: ViewDate;
	isToday = true;
	isYesterday = false;
	isTomorrow = false;
	actualDate: Date = new Date();
	entryDate: Date;
	currentViewIsEntryDate = false;
	dateTime: DateTimeDto;
	professionalId: number;
	diets: DietDto[] = [];
	diagnostics: DiagnosesGeneralStateDto[] = [];
	otherIndications: OtherIndicationDto[] = [];
	othersIndicatiosType: OtherIndicationTypeDto[];
	parenteralPlan: ParenteralPlanDto[] = [];
	pharmacos: PharmacoDto[] = [];
	@Input() internmentEpisodeId: number;
	@Input() epicrisisConfirmed: boolean;
	@Input() patientId: number;
	ACTIVE_STATE = "Activo";
	clinicalStatus: MasterDataInterface<string>[];

	constructor(
		private readonly dialog: MatDialog,
		private readonly snackBarService: SnackBarService,
		private readonly indicationsFacadeService: IndicationsFacadeService,
		private readonly internmentEpisode: InternmentEpisodeService,
		private readonly internmentIndicationService: InternmentIndicationService,
		private readonly internmentStateService: InternmentStateService,
		private readonly internacionMasterdataService: InternacionMasterDataService,
		private readonly healthcareProfessionalService: HealthcareProfessionalService
	) { }

	ngOnInit(): void {
		this.viewDay = {
			nameDay: DAYS_OF_WEEK[getDay(this.actualDate)],
			numberDay: this.actualDate.getDate(),
			month: MONTHS_OF_YEAR[getMonth(this.actualDate)]
		};
		this.internmentEpisode.getInternmentEpisode(this.internmentEpisodeId).subscribe(
			internmentEpisode => {
				this.entryDate = new Date(internmentEpisode.entryDate);
				const differenceInDays = differenceInCalendarDays(this.actualDate, this.entryDate);
				if (differenceInDays <= 0)
					this.currentViewIsEntryDate = true;
			}
		);
		this.healthcareProfessionalService.getHealthcareProfessionalByUserId().subscribe((professionalId: number) => this.professionalId = professionalId);
		this.indicationsFacadeService.setInternmentEpisodeId(this.internmentEpisodeId);
		this.filterIndications();
		this.internmentIndicationService.getOtherIndicationTypes().subscribe((othersIndicationsType: OtherIndicationTypeDto[]) => this.othersIndicatiosType = othersIndicationsType);
		this.internmentStateService.getDiagnosesGeneralState(this.internmentEpisodeId).subscribe((diagnostics: DiagnosesGeneralStateDto[]) => {
			if (diagnostics)
				this.internacionMasterdataService.getHealthClinical().subscribe(healthClinical => {

					this.clinicalStatus = healthClinical?.filter(s => s.description === this.ACTIVE_STATE);
					this.diagnostics = diagnostics?.filter(d => this.clinicalStatus.find(e => e?.id === d?.statusId));

				});
		});
	}

	viewAnotherDay(daysToMove: number) {
		this.actualDate.setDate(this.actualDate.getDate() + daysToMove);
		this.isToday = isToday(this.actualDate);
		this.isTomorrow = isTomorrow(this.actualDate);
		this.isYesterday = isYesterday(this.actualDate);
		this.loadDay();
	}

	private loadDay() {
		const differenceInDays = differenceInCalendarDays(this.actualDate, this.entryDate);
		if (differenceInDays >= 0) {
			this.currentViewIsEntryDate = false;
			this.viewDay = {
				nameDay: DAYS_OF_WEEK[getDay(this.actualDate)],
				numberDay: this.actualDate.getDate(),
				month: MONTHS_OF_YEAR[getMonth(this.actualDate)]
			};
			this.filterIndications();
		}
		if (differenceInDays <= 0)
			this.currentViewIsEntryDate = true;
	}

	openDietDialog() {
		const dialogRef = this.dialog.open(DietComponent, {
			data: {
				entryDate: this.entryDate,
				actualDate: this.actualDate,
				patientId: this.patientId,
				professionalId: this.professionalId
			},
			disableClose: false,
			width: DIALOG_SIZE
		});

		dialogRef.afterClosed().subscribe((diet: DietDto) => {
			if (diet) {
				this.indicationsFacadeService.addDiet(diet).subscribe(_ => {
					this.snackBarService.showSuccess('indicacion.internment-card.dialogs.diet.messages.SUCCESS');
					this.indicationsFacadeService.updateIndication({ diets: true });
				},
					error => {
						error?.text ?
							this.snackBarService.showError(error.text) : this.snackBarService.showError('indicacion.internment-card.dialogs.diet.messages.ERROR');
					}
				);
			}
		});
	}

	filterIndications() {
		this.indicationsFacadeService.diets$.subscribe(d => this.diets = d.filter((diet: DietDto) => isSameDay(dateDtoToDate(diet.indicationDate), this.actualDate) === true));
		this.indicationsFacadeService.otherIndications$.subscribe(d => this.otherIndications = d.filter((otherIndications: OtherIndicationDto) => isSameDay(dateDtoToDate(otherIndications.indicationDate), this.actualDate)));
		this.indicationsFacadeService.parenteralPlans$.subscribe(p => this.parenteralPlan = p.filter((plan: ParenteralPlanDto) => isSameDay(dateDtoToDate(plan.indicationDate), this.actualDate) === true));
		this.indicationsFacadeService.pharmacos$.subscribe(p => this.pharmacos = p.filter((pharmaco: PharmacoDto) => isSameDay(dateDtoToDate(pharmaco.indicationDate), this.actualDate)));
	}

	openPharmacoDialog() {
		const dialogRef = this.dialog.open(PharmacoComponent, {
			data: {
				entryDate: this.entryDate,
				actualDate: this.actualDate,
				patientId: this.patientId,
				professionalId: this.professionalId,
				diagnostics: this.diagnostics
			},
			autoFocus: true,
			disableClose: false
		});

		dialogRef.afterClosed().subscribe((pharmaco: PharmacoDto) => {

			if (pharmaco) {
				this.indicationsFacadeService.addPharmaco(pharmaco).subscribe(_ => {
					this.snackBarService.showSuccess('indicacion.internment-card.dialogs.pharmaco.messages.SUCCESS');
					this.indicationsFacadeService.updateIndication({ pharmaco: true });
				},
					error => {
						error?.text ?
							this.snackBarService.showError(error.text) : this.snackBarService.showError('indicacion.internment-card.dialogs.pharmaco.messages.ERROR');
					});
			}
		});
	}

	openIndicationDialog() {
		const dialogRef = this.dialog.open(OtherIndicationComponent, {
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

		dialogRef.afterClosed().subscribe(otherIndicatio => {

			if (otherIndicatio) {
				this.indicationsFacadeService.addOtherIndication(otherIndicatio).subscribe(_ => {
					this.snackBarService.showSuccess('indicacion.internment-card.dialogs.other-indication.messages.SUCCESS');
					this.indicationsFacadeService.updateIndication({ otherIndication: true });
				},
					error => {
						error?.text ?
							this.snackBarService.showError(error.text) : this.snackBarService.showError('indicacion.internment-card.dialogs.other-indication.messages.ERROR');
					});
			}
		});
	}



	openParenteralPlanDialog() {
		const dialogRef = this.dialog.open(ParenteralPlanComponent, {
			data: {
				entryDate: this.entryDate,
				actualDate: this.actualDate,
				patientId: this.patientId,
				professionalId: this.professionalId
			},
			autoFocus: false,
			disableClose: true,
		});
		dialogRef.afterClosed().subscribe((parenteralPlan: ParenteralPlanDto) => {
			if (parenteralPlan) {
				this.indicationsFacadeService.addParenteralPlan(parenteralPlan).subscribe(
					success => {
						this.snackBarService.showSuccess('indicacion.internment-card.dialogs.parenteral-plan.messages.SUCCESS');
						this.indicationsFacadeService.updateIndication({ parenteralPlan: true });
					},
					error => error?.text ? this.snackBarService.showError(error.text) : this.snackBarService.showError('indicacion.internment-card.dialogs.parenteral-plan.messages.ERROR')
				);
			}
		});
	}
}

interface ViewDate {
	nameDay: string,
	numberDay: number,
	month: string,
}

