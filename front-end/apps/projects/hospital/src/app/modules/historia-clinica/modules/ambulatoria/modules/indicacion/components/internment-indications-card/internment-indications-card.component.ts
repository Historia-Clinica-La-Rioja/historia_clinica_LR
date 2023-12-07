import { Component, Input, OnInit, } from '@angular/core';
import { INTERNMENT_INDICATIONS } from "@historia-clinica/constants/summaries";
import { isSameDay } from "date-fns";
import { InternmentEpisodeService } from "@api-rest/services/internment-episode.service";
import { DiagnosesGeneralStateDto, DietDto, OtherIndicationDto, ParenteralPlanDto, PharmacoDto, PharmacoSummaryDto } from "@api-rest/api-model";

import { HealthcareProfessionalService } from '@api-rest/services/healthcare-professional.service';
import { IndicationsFacadeService } from "@historia-clinica/modules/ambulatoria/modules/indicacion/services/indications-facade.service";
import { dateDtoToDate } from "@api-rest/mapper/date-dto.mapper";
import { InternmentIndicationService, OtherIndicationTypeDto } from '@api-rest/services/internment-indication.service';


import { ActionsButtonService } from '../../services/actions-button.service';
import { INDICATION_TYPE } from '../../constants/internment-indications';
import { IndicationByProfessionalService } from '@api-rest/services/indication-by-professional.service';
import { ConfirmDialogComponent } from '@presentation/dialogs/confirm-dialog/confirm-dialog.component';
import { InternacionMasterDataService } from '@api-rest/services/internacion-master-data.service';
import { InternmentStateService } from '@api-rest/services/internment-state.service';
import { MatDialog } from '@angular/material/dialog';
import { SnackBarService } from '@presentation/services/snack-bar.service';

@Component({
	selector: 'app-internment-indications-card',
	templateUrl: './internment-indications-card.component.html',
	styleUrls: ['./internment-indications-card.component.scss'],
	providers: [ActionsButtonService]

})
export class InternmentIndicationsCardComponent implements OnInit {
	internmentIndication = INTERNMENT_INDICATIONS;
	actualDate: Date;
	entryDate: Date;
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

	constructor(
		private readonly indicationsFacadeService: IndicationsFacadeService,
		private readonly internmentEpisode: InternmentEpisodeService,
		private readonly internmentIndicationService: InternmentIndicationService,
		private readonly healthcareProfessionalService: HealthcareProfessionalService,
		private readonly actionsButtonService: ActionsButtonService,
		private readonly indicationByProfessionalService: IndicationByProfessionalService,
		private readonly internacionMasterdataService: InternacionMasterDataService,
		private readonly internmentStateService: InternmentStateService,
		private readonly dialog: MatDialog,
		private readonly snackBarService: SnackBarService,
	) {
	}

	ngOnInit(): void {
		this.actionsButtonService.internmentEpisodeId = this.internmentEpisodeId;
		this.actionsButtonService.notShowActionButton = this.epicrisisConfirmed;
		this.actionsButtonService.patientId = this.patientId;
		this.internmentEpisode.getInternmentEpisode(this.internmentEpisodeId).subscribe(
			internmentEpisode => {
				this.entryDate = new Date(internmentEpisode.entryDate);
				this.actionsButtonService.entryDate = this.entryDate;
			}
		);

		this.healthcareProfessionalService.getHealthcareProfessionalByUserId().subscribe((professionalId: number) => this.professionalId = professionalId);

		this.indicationsFacadeService.setInternmentEpisodeId(this.internmentEpisodeId);

		this.internmentIndicationService.getOtherIndicationTypes().subscribe((othersIndicationsType: OtherIndicationTypeDto[]) => this.othersIndicatiosType = othersIndicationsType);
	}

	openDiet() {
		const ref = this.actionsButtonService.openDialog(INDICATION_TYPE.DIET);
		ref.afterClosed().subscribe((diet: DietDto) => {
			if (diet) {
				this.internmentIndicationService.addDiet(diet, this.internmentEpisodeId).subscribe(_ => {
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

	openParenteralPlan() {
		this.indicationByProfessionalService.getMostFrequentParenteralPlan().subscribe((parenteralPLanSpecialist: ParenteralPlanDto[]) => {
			const dialogPharmacosFrequent = this.actionsButtonService.openDialog(INDICATION_TYPE.PARENTERAL_PLAN, parenteralPLanSpecialist);
			dialogPharmacosFrequent.afterClosed().subscribe((result: DialogPharmacosFrequent<ParenteralPlanDto>) => {
				if (result?.openFormPharmaco) {
					const ref = this.actionsButtonService.openParenteralPlanDialog(result.pharmaco);
					ref.afterClosed().subscribe((resultDialogPharmaco: ResultDialogPharmaco<ParenteralPlanDto>) => {
						if (resultDialogPharmaco?.pharmaco) {
							this.indicationsFacadeService.addParenteralPlan(resultDialogPharmaco.pharmaco).subscribe(
								success => {
									this.snackBarService.showSuccess('indicacion.internment-card.dialogs.parenteral-plan.messages.SUCCESS');
									this.indicationsFacadeService.updateIndication({ parenteralPlan: true });
								}
								, error => error?.text ? this.snackBarService.showError(error.text) : this.snackBarService.showError('indicacion.internment-card.dialogs.parenteral-plan.messages.ERROR')
							);
						} else {
							if (resultDialogPharmaco?.openDialogPharmacosFrequent)
								this.actionsButtonService.openMostFrequentParenteralPlansDialog(parenteralPLanSpecialist);
						}
					});
				}
			})
		})
	}

	openPharmacos() {
		this.indicationByProfessionalService.getMostFrequentPharmacos().subscribe((pharmacos: PharmacoSummaryDto[]) => {
			let recentlyPrescribedPharmacos = filterLastThreeDays(this.pharmacos);
			let pharmacosWithoutRepetition = filterUniqueBySctidAndQuantity(recentlyPrescribedPharmacos);
			pharmacos = pharmacosWithoutRepetition.concat(pharmacos);
			const mostFrequent = pharmacos.map((p: PharmacoSummaryDto) => {
				return { description: p.snomed.pt, value: p }
			});
			const ref = this.actionsButtonService.openDialog(INDICATION_TYPE.PHARMACO, mostFrequent);
			ref.afterClosed().subscribe((result: DialogPharmacosFrequent<PharmacoSummaryDto>) => {
				if (result?.openFormPharmaco) {
					this.internmentStateService.getDiagnosesGeneralState(this.internmentEpisodeId).subscribe((diagnostics: DiagnosesGeneralStateDto[]) => {
						if (diagnostics)
							this.internacionMasterdataService.getHealthClinical().subscribe(healthClinical => {
								const clinicalStatus = healthClinical?.filter(s => s.description === this.actionsButtonService.ACTIVE_STATE);
								this.diagnostics = diagnostics?.filter(d => clinicalStatus.find(e => e?.id === d?.statusId));


								if (this.diagnostics?.length) {

									const ref = this.actionsButtonService.openFormPharmacoDialog(result.pharmaco, this.diagnostics);
									ref.afterClosed().subscribe((result: ResultDialogPharmaco<PharmacoDto>) => {
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
											this.actionsButtonService.openPharmacoDialog(mostFrequent);
									});
								}
								else {
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
			})
		})

		function filterLastThreeDays(patientPharmacos) {
			const currentDate = new Date();
			const threeDaysAgo = new Date();
			threeDaysAgo.setDate(currentDate.getDate() - 3);

			const result = patientPharmacos.filter((element) => {
				const indicationDate = new Date(
					element.indicationDate.year,
					element.indicationDate.month - 1,
					element.indicationDate.day
				);

				return indicationDate >= threeDaysAgo && indicationDate <= currentDate;
			});

			return result;
		}

		function filterUniqueBySctidAndQuantity(recentlyPrescribedPharmacos) {
			const uniqueRecords = {};
			const result = recentlyPrescribedPharmacos.filter((element) => {
				const sctid = element.snomed.sctid;
				const quantity = element.dosage.quantity.value;
				const key = `${sctid}_${quantity}`;

				if (uniqueRecords[key]) {
					return false;
				}

				uniqueRecords[key] = true;
				return true;
			});

			return result;
		}

	}

	openOtherIndication() {
		const ref = this.actionsButtonService.openDialog(INDICATION_TYPE.OTHER_INDICATION);
		ref.afterClosed().subscribe(otherIndicatio => {

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

	loadActualDateAndFilter(actualDate: Date) {
		this.actionsButtonService.actualDate = actualDate;
		this.actualDate = actualDate;
		this.filterIndications();
	}

	filterIndications() {
		this.indicationsFacadeService.diets$.subscribe(d => this.diets = d.filter((diet: DietDto) => isSameDay(dateDtoToDate(diet.indicationDate), this.actualDate)));
		this.indicationsFacadeService.otherIndications$.subscribe(d => this.otherIndications = d.filter((otherIndications: OtherIndicationDto) => isSameDay(dateDtoToDate(otherIndications.indicationDate), this.actualDate)));
		this.indicationsFacadeService.parenteralPlans$.subscribe(p => this.parenteralPlan = p.filter((plan: ParenteralPlanDto) => isSameDay(dateDtoToDate(plan.indicationDate), this.actualDate)));
		this.indicationsFacadeService.pharmacos$.subscribe(p => this.pharmacos = p.filter((pharmaco: PharmacoDto) => isSameDay(dateDtoToDate(pharmaco.indicationDate), this.actualDate)));
	}

}

export interface DialogPharmacosFrequent<T> {
	openFormPharmaco: boolean;
	pharmaco: T;
}

interface ResultDialogPharmaco<T> {
	openDialogPharmacosFrequent: boolean;
	pharmaco?: T;
}

