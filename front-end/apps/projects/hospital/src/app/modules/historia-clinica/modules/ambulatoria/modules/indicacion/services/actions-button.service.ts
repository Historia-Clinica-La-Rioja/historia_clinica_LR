import { Injectable } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { DateDto, DiagnosesGeneralStateDto, DietDto, MasterDataInterface, ParenteralPlanDto, PharmacoDto, PharmacoSummaryDto } from '@api-rest/api-model';
import { HealthcareProfessionalService } from '@api-rest/services/healthcare-professional.service';
import { IndicationByProfessionalService } from '@api-rest/services/indication-by-professional.service';
import { InternmentEpisodeService } from '@api-rest/services/internment-episode.service';
import { InternmentIndicationService, OtherIndicationTypeDto } from '@api-rest/services/internment-indication.service';
import { InternmentStateService } from '@api-rest/services/internment-state.service';
import { ConfirmDialogComponent } from '@presentation/dialogs/confirm-dialog/confirm-dialog.component';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { BehaviorSubject } from 'rxjs';
import { DialogPharmacosFrequent } from '../components/internment-indications-card/internment-indications-card.component';
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
	private internmentId: number;
	private entryDate: Date;
	private professionalId: number;
	private othersIndicatiosType: OtherIndicationTypeDto[];
	private internmentEpisodeIdSubject = new BehaviorSubject<number>(null);
	private internmentEpisodeIdSubject$ = this.internmentEpisodeIdSubject.asObservable();
	private epicrisisConfirmedSubject = new BehaviorSubject<boolean>(false);
	private clinicalStatus: MasterDataInterface<string>[];
	private diagnostics: DiagnosesGeneralStateDto[] = [];
	private vias: MasterDataInterface<number>[] = [];
	private units: MasterDataInterface<number>[] = [];

	constructor(
		private readonly dialog: MatDialog,
		private readonly snackBarService: SnackBarService,
		private readonly indicationsFacadeService: IndicationsFacadeService,
		private readonly internmentEpisodeService: InternmentEpisodeService,
		private readonly healthcareProfessionalService: HealthcareProfessionalService,
		private readonly internmentIndicationService: InternmentIndicationService,
		private readonly indicationByProfessionalService: IndicationByProfessionalService,
		private readonly internmentStateService: InternmentStateService,
		private readonly internacionMasterdataService: InternacionMasterDataService,


	) {
		this.internacionMasterdataService.getVias().subscribe(v => this.vias = v);
		this.internacionMasterdataService.getUnits().subscribe(u => this.units = u);

		this.internmentEpisodeIdSubject$.subscribe(internmentEpisodeId => {
			if (internmentEpisodeId) {
				this.internmentId = internmentEpisodeId;
				this.internmentEpisodeService.getInternmentEpisode(internmentEpisodeId).subscribe(
					internmentEpisode => this.entryDate = new Date(internmentEpisode.entryDate)
				);
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

	openDialog(indication: any) {
		switch (indication) {
			case INDICATION_TYPE.DIET: {
				this.openDietDialog();
			} break
			case INDICATION_TYPE.OTHER_INDICATION: {
				this.openOtherIndicationDialog();
			} break
			case INDICATION_TYPE.PHARMACO: {
				this.openPharmacoDialog();
			} break
			case INDICATION_TYPE.PARENTERAL_PLAN: {
				this.openMostFrequentParenteralPlansDialog();
			} break
		}
	}


	openOtherIndicationDialog() {
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

	openPharmacoDialog() {
		this.indicationByProfessionalService.getMostFrequentPharmacos().subscribe((pharmacos: PharmacoSummaryDto[]) => {
			const mostFrequent = pharmacos.map((p: PharmacoSummaryDto) => {
				return { description: p.snomed.pt, value: p }
			});
			const dialogPharmacosFrequent = this.dialog.open(MostFrequentComponent, {
				width: '50%',
				data: {
					items: mostFrequent,
					title: 'indicacion.card-pharmaco-frequent.TITLE'
				}
			});
			dialogPharmacosFrequent.afterClosed().subscribe((result: DialogPharmacosFrequent<PharmacoSummaryDto>) => {
				if (result?.openFormPharmaco)
					this.openFormPharmacoDialog(result.pharmaco);
			});
		});
	}

	openFormPharmacoDialog(pharmaco: PharmacoSummaryDto) {
		this.internmentStateService.getDiagnosesGeneralState(this.internmentId).subscribe((diagnostics: DiagnosesGeneralStateDto[]) => {
			if (diagnostics)
				this.internacionMasterdataService.getHealthClinical().subscribe(healthClinical => {
					this.clinicalStatus = healthClinical?.filter(s => s.description === this.ACTIVE_STATE);
					this.diagnostics = diagnostics?.filter(d => this.clinicalStatus.find(e => e?.id === d?.statusId));

					if (this.diagnostics?.length) {
						const dialogRef = this.dialog.open(PharmacoComponent, {
							data: {
								entryDate: this.entryDate,
								actualDate: this.actualDate,
								patientId: this.patientId,
								professionalId: this.professionalId,
								diagnostics: this.diagnostics,
								vias: this.vias,
								units: this.units,
								pharmaco
							},
							autoFocus: true,
							disableClose: false
						});

						dialogRef.afterClosed().subscribe((result: ResultDialogPharmaco<PharmacoDto>) => {
							if (result?.pharmaco) {
								this.indicationsFacadeService.addPharmaco(result.pharmaco).subscribe(_sucess => {
									this.snackBarService.showSuccess('indicacion.internment-card.dialogs.pharmaco.messages.SUCCESS');
									this.indicationsFacadeService.updateIndication({ pharmaco: true });
								},
									error => {
										error?.text ?
											this.snackBarService.showError(error.text) : this.snackBarService.showError('indicacion.internment-card.dialogs.pharmaco.messages.ERROR');
									});
							}
							if (result?.openDialogPharmacosFrequent)
								this.openPharmacoDialog();
						});
					} else {
						this.dialog.open(ConfirmDialogComponent, { data: getConfirmDataDialog() });
						function getConfirmDataDialog() {
							const keyPrefix = 'indicacion.internment-card.dialogs.pharmaco.messages';
							return {
								showMatIconError: true,
								title: `${keyPrefix}.TITLE`,
								content: `${keyPrefix}.CONTENT`,
								okButtonLabel: `${keyPrefix}.OK_BUTTON`,
							};
						}
					}
				});
		})
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


	openParenteralPlanDialog(parenteralPlan?: ParenteralPlanDto) {
		const dialogRef = this.dialog.open(ParenteralPlanComponent, {
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
		dialogRef.afterClosed().subscribe((resultDialogPharmaco: ResultDialogPharmaco<ParenteralPlanDto>) => {
			if (resultDialogPharmaco?.pharmaco) {
				this.indicationsFacadeService.addParenteralPlan(resultDialogPharmaco.pharmaco).subscribe(
					success => {
						this.snackBarService.showSuccess('indicacion.internment-card.dialogs.parenteral-plan.messages.SUCCESS');
						this.indicationsFacadeService.updateIndication({ parenteralPlan: true });
					},
					error => error?.text ? this.snackBarService.showError(error.text) : this.snackBarService.showError('indicacion.internment-card.dialogs.parenteral-plan.messages.ERROR')
				);
			} else {
				if (resultDialogPharmaco?.openDialogPharmacosFrequent)
					this.openMostFrequentParenteralPlansDialog();
			}
		});
	}
	openMostFrequentParenteralPlansDialog() {
		this.indicationByProfessionalService.getMostFrequentParenteralPlan().subscribe((parenteralPLanSpecialist: ParenteralPlanDto[]) => {
			const dialogPharmacosFrequent = this.dialog.open(MostFrequentComponent,
				{
					width: '50%',
					data: {
						items: this.getParenteralPlansFrequent(parenteralPLanSpecialist),
						title: 'indicacion.most-frequent.TITLE_PARENTERAL'
					}
				});
			dialogPharmacosFrequent.afterClosed().subscribe((result: DialogPharmacosFrequent<ParenteralPlanDto>) => {
				if (result?.openFormPharmaco)
					this.openParenteralPlanDialog(result.pharmaco);
			});
		});
	}


	private getParenteralPlansFrequent(parenteralPLanSpecialist: ParenteralPlanDto[]): Item<ParenteralPlanDto>[] {

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
