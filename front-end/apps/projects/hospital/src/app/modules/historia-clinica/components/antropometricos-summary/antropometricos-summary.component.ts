import { Component, Input, OnDestroy, OnInit } from '@angular/core';
import { ANTROPOMETRICOS } from '../../constants/summaries';
import { DetailBox } from '@presentation/components/detail-box/detail-box.component';
import { MatDialog } from '@angular/material/dialog';
import { AddAnthropometricComponent } from '../../dialogs/add-anthropometric/add-anthropometric.component';
import { InternmentSummaryFacadeService } from "@historia-clinica/modules/ambulatoria/modules/internacion/services/internment-summary-facade.service";
import { AnthropometricDataDto, HCEAnthropometricDataDto } from '@api-rest/api-model';
import { Observable, Subscription } from 'rxjs';
import { ActivatedRoute, Router } from '@angular/router';
import { PatientEvolutionChartsService } from '@historia-clinica/services/patient-evolution-charts.service';
import { getParam } from '@historia-clinica/modules/ambulatoria/modules/estudio/utils/utils';

const URL_ACCESO_PACIENTE = "/paciente";
@Component({
	selector: 'app-antropometricos-summary',
	templateUrl: './antropometricos-summary.component.html',
	styleUrls: ['./antropometricos-summary.component.scss']
})
export class AntropometricosSummaryComponent implements OnInit, OnDestroy {

	@Input() internmentEpisodeId: number;
	@Input() anthropometricDataList$: Observable<HCEAnthropometricDataDto[]> | Observable<AnthropometricDataDto[]>;
	@Input() editable = false;

	details: DetailBoxExtended[] = [];
	readonly antropometricosSummary = ANTROPOMETRICOS;
	subscriptionAnthropometricData: Subscription;
	patientId: number;

	private readonly LABELS = {
		height: 'Talla (cm)',
		weight: 'Peso (kg)',
		bmi: 'IMC',
		headCircumference: 'Perímetro cefálico (cm)',
	};

	constructor(
		public dialog: MatDialog,
		readonly patientEvolutionChartService: PatientEvolutionChartsService,
		private readonly internmentSummaryFacadeService: InternmentSummaryFacadeService,
		private readonly activatedRoute: ActivatedRoute,
		public readonly router: Router,
	) { }

	ngOnDestroy(): void {
		this.subscriptionAnthropometricData?.unsubscribe();
	}

	ngOnInit(): void {
		this.patientId = Number(getParam(this.activatedRoute.snapshot, 'idPaciente'));
		this.patientEvolutionChartService.patientId = this.patientId;
		if(this.router.url !== URL_ACCESO_PACIENTE ){
			this.subscriptionAnthropometricData = this.anthropometricDataList$.subscribe(list => {
				this.updateAnthropometricData(list);
				this.patientEvolutionChartService.updateButtonEnablementByPatientInfo();
			});
		}
	}

	openDialog(): void {
		const dialogRef = this.dialog.open(AddAnthropometricComponent, {
			disableClose: true,
			width: '25%',
			data: {
				internmentEpisodeId: this.internmentEpisodeId,
				patientId : this.patientId
			}
		});

		dialogRef.afterClosed().subscribe(fieldsToUpdate => {
			if (fieldsToUpdate) {
				this.internmentSummaryFacadeService.setFieldsToUpdate({
					heightAndWeight: fieldsToUpdate.heightAndWeight,
					bloodType: fieldsToUpdate.bloodType,
					evolutionClinical: true
				});
			}
		}
		);
	}

	private getIdentificator(description: string): string {
		return description.split(' (')[0]
			.split(" ").join('-').toLowerCase();
	}

	private truncateIfNecessary(floatValue: string): string {
		if (!floatValue)
			return undefined;

		const lastPartValue = floatValue.substring(floatValue.length - 3);
		if (lastPartValue === '.00') {
			return floatValue.substring(0, floatValue.length - 3);
		}

		if (lastPartValue[2] === '0') {
			return floatValue.substring(0, floatValue.length - 1);
		}

		return floatValue;
	}

	private updateAnthropometricData(list: HCEAnthropometricDataDto[] | AnthropometricDataDto[]): void {
		if (list?.length > 0) {
			let details: DetailBoxExtended[] = [];

			for (let i = 0; i < 2 && i < list.length; i++) {
				if (list[i]?.bmi?.value) {
					list[i].bmi.value = this.truncateIfNecessary(list[i].bmi.value);
				}
			}

			Object.keys(this.LABELS).forEach(
				key => {
					const lastAnthropometricData = list[0];
					if (lastAnthropometricData[key]?.value) {
						let detail: DetailBoxExtended = {
							description: this.LABELS[key],
							id: this.getIdentificator(this.LABELS[key]),
							registeredValues: [
								{
									date: lastAnthropometricData[key]?.effectiveTime,
									value: lastAnthropometricData[key].value,
								}
							],
						}

						if (list.length > 1) {
							const previousAnthropometricData = list[1];
							if (previousAnthropometricData[key]?.value) {
								detail.registeredValues.push(
									{
										date: previousAnthropometricData[key]?.effectiveTime,
										value: previousAnthropometricData[key].value,
									}
								);

							}
						}

						details.push(detail);
					}
				}
			);

			this.details = details;
		} else {
			this.details = [];
		}
	}

}

interface DetailBoxExtended extends DetailBox {
	id: string;
}
