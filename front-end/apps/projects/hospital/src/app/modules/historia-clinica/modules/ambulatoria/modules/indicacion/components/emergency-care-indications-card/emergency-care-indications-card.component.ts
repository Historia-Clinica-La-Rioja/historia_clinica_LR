import { Component, Input, OnInit } from '@angular/core';
import { DiagnosesGeneralStateDto, DietDto, OtherIndicationDto, ParenteralPlanDto, PharmacoDto, PharmacoSummaryDto, ResponseEmergencyCareDto } from '@api-rest/api-model';
import { HealthcareProfessionalService } from '@api-rest/services/healthcare-professional.service';
import { EMERGENCY_CARE_INDICATIONS } from '@historia-clinica/constants/summaries';
import { ActionsButtonService } from '../../services/actions-button.service';
import { isSameDay } from 'date-fns';
import { dateDtoToDate, dateTimeDtoToDate } from '@api-rest/mapper/date-dto.mapper';
import { EmergencyCareIndicationsFacadeService } from '../../services/emergency-care-indications-facade.service';
import { EmergencyCareEpisodeService } from '@api-rest/services/emergency-care-episode.service';
import { EmergencyCareIndicationService } from '@api-rest/services/emergency-care-indication.service';
import { OtherIndicationTypeDto } from '@api-rest/services/internment-indication.service';
import { INDICATION_TYPE } from '../../constants/internment-indications';
import { IndicationByProfessionalService } from '@api-rest/services/indication-by-professional.service';
import { EmergencyCareStateService } from '@api-rest/services/emergency-care-state.service';
import { InternacionMasterDataService } from '@api-rest/services/internacion-master-data.service';
import { ConfirmDialogComponent } from '@presentation/dialogs/confirm-dialog/confirm-dialog.component';
import { MatDialog } from '@angular/material/dialog';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { EstadosEpisodio } from '@historia-clinica/modules/guardia/constants/masterdata';


@Component({
	selector: 'app-emergency-care-indications-card',
	templateUrl: './emergency-care-indications-card.component.html',
	styleUrls: ['./emergency-care-indications-card.component.scss'],
	providers: [ActionsButtonService]
})
export class EmergencyCareIndicationsCardComponent implements OnInit {
	emergencyCareIndication = EMERGENCY_CARE_INDICATIONS;
	actualDate: Date;
	entryDate: Date;
	professionalId: number;
	diets: DietDto[] = [];
	diagnostics: DiagnosesGeneralStateDto[] = [];
	otherIndications: OtherIndicationDto[] = [];
	othersIndicatiosType: OtherIndicationTypeDto[];
	parenteralPlan: ParenteralPlanDto[] = [];
	pharmacos: PharmacoDto[] = [];
	@Input() emergencyCareEpisodeId: number;
	@Input() patientId: number;

	constructor(
		private readonly emergencyCareIndicationsFacadeService: EmergencyCareIndicationsFacadeService,
		private readonly emergencyCareEpisodeService: EmergencyCareEpisodeService,
		private readonly emergencyCareIndicationService: EmergencyCareIndicationService, // cpoinciden asique vamos a reutilizarlo ( podriamos mover el servicio tanto en FE como BE)
		private readonly healthcareProfessionalService: HealthcareProfessionalService,
		private readonly actionsButtonService: ActionsButtonService,
		private readonly indicationByProfessionalService: IndicationByProfessionalService,
		private readonly emergencyCareStateService: EmergencyCareStateService,
		private readonly internacionMasterdataService: InternacionMasterDataService,
		private readonly dialog: MatDialog,
		private readonly snackBarService: SnackBarService,
	) {
	}

	ngOnInit(): void {
		this.actionsButtonService.internmentEpisodeId = this.emergencyCareEpisodeId;
		this.actionsButtonService.patientId = this.patientId;

		this.emergencyCareEpisodeService.getAdministrative(this.emergencyCareEpisodeId).subscribe(
			(emergencyCareEpisode: ResponseEmergencyCareDto) => {
				this.entryDate = dateTimeDtoToDate(emergencyCareEpisode.creationDate);
				this.actionsButtonService.entryDate = this.entryDate;
				this.actionsButtonService.notShowActionButton = emergencyCareEpisode.emergencyCareState.id !== EstadosEpisodio.EN_ATENCION
			}
		)

		this.healthcareProfessionalService.getHealthcareProfessionalByUserId().subscribe((professionalId: number) => this.professionalId = professionalId);

		this.emergencyCareIndicationsFacadeService.setEmergencyCareEpisodeId(this.emergencyCareEpisodeId);

		this.emergencyCareIndicationService.getOtherIndicationTypes().subscribe((othersIndicationsType: OtherIndicationTypeDto[]) => this.othersIndicatiosType = othersIndicationsType);
	}

	openDiet() {
		const ref = this.actionsButtonService.openDialog(INDICATION_TYPE.DIET);
		ref.afterClosed().subscribe((diet: DietDto) => {
			if (diet) {
				this.emergencyCareIndicationService.addDiet(diet, this.emergencyCareEpisodeId).subscribe(_ => {
					this.snackBarService.showSuccess('indicacion.internment-card.dialogs.diet.messages.SUCCESS');
					this.emergencyCareIndicationsFacadeService.updateIndication({ diets: true });
				},
					error => {
						error?.text ?
							this.snackBarService.showError(error.text) : this.snackBarService.showError('indicacion.internment-card.dialogs.diet.messages.ERROR');
					}
				);
			}
		});
	}

	openOtherIndication() {
		const ref = this.actionsButtonService.openDialog(INDICATION_TYPE.OTHER_INDICATION);
		ref.afterClosed().subscribe(otherIndicatio => {

			if (otherIndicatio) {
				this.emergencyCareIndicationsFacadeService.addOtherIndication(otherIndicatio).subscribe(_ => {
					this.snackBarService.showSuccess('indicacion.internment-card.dialogs.other-indication.messages.SUCCESS');
					this.emergencyCareIndicationsFacadeService.updateIndication({ otherIndication: true });
				},
					error => {
						error?.text ?
							this.snackBarService.showError(error.text) : this.snackBarService.showError('indicacion.internment-card.dialogs.other-indication.messages.ERROR');
					});
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
							this.emergencyCareIndicationsFacadeService.addParenteralPlan(resultDialogPharmaco.pharmaco).subscribe(
								success => {
									this.snackBarService.showSuccess('indicacion.internment-card.dialogs.parenteral-plan.messages.SUCCESS');
									this.emergencyCareIndicationsFacadeService.updateIndication({ parenteralPlan: true });
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
					this.emergencyCareStateService.getEmergencyCareEpisodeDiagnosesWithoutNursingAttentionDiagnostic(this.emergencyCareEpisodeId).subscribe((diagnostics: DiagnosesGeneralStateDto[]) => {
						if (diagnostics)
							this.internacionMasterdataService.getHealthClinical().subscribe(healthClinical => {
								const clinicalStatus = healthClinical?.filter(s => s.description === this.actionsButtonService.ACTIVE_STATE);
								this.diagnostics = diagnostics?.filter(d => clinicalStatus.find(e => e?.id === d?.statusId));


								if (this.diagnostics?.length) {

									const ref = this.actionsButtonService.openFormPharmacoDialog(result.pharmaco, this.diagnostics);
									ref.afterClosed().subscribe((result: ResultDialogPharmaco<PharmacoDto>) => {
										if (result?.pharmaco) {
											this.emergencyCareIndicationsFacadeService.addPharmaco(result.pharmaco).subscribe(_sucess => {
												this.snackBarService.showSuccess('indicacion.internment-card.dialogs.pharmaco.messages.SUCCESS');
												this.emergencyCareIndicationsFacadeService.updateIndication({ pharmaco: true });
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



	loadActualDateAndFilter(actualDate: Date) {
		this.actionsButtonService.actualDate = actualDate;
		this.actualDate = actualDate;
		this.filterIndications();
	}

	filterIndications() {
		this.emergencyCareIndicationsFacadeService.diets$.subscribe(d => this.diets = d.filter((diet: DietDto) => isSameDay(dateDtoToDate(diet.indicationDate), this.actualDate)));
		this.emergencyCareIndicationsFacadeService.otherIndications$.subscribe(d => this.otherIndications = d.filter((otherIndications: OtherIndicationDto) => isSameDay(dateDtoToDate(otherIndications.indicationDate), this.actualDate)));
		this.emergencyCareIndicationsFacadeService.parenteralPlans$.subscribe(p => this.parenteralPlan = p.filter((plan: ParenteralPlanDto) => isSameDay(dateDtoToDate(plan.indicationDate), this.actualDate)));
		this.emergencyCareIndicationsFacadeService.pharmacos$.subscribe(p => this.pharmacos = p.filter((pharmaco: PharmacoDto) => isSameDay(dateDtoToDate(pharmaco.indicationDate), this.actualDate)));
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

