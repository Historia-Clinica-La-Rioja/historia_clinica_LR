import { Component, Input, OnInit } from '@angular/core';
import { ANTROPOMETRICOS } from '../../constants/summaries';
import { DetailBox } from '@presentation/components/detail-box/detail-box.component';
import { MatDialog } from '@angular/material/dialog';
import { AddAnthropometricComponent } from '../../dialogs/add-anthropometric/add-anthropometric.component';
import { InternmentSummaryFacadeService } from "@historia-clinica/modules/ambulatoria/modules/internacion/services/internment-summary-facade.service";
import { AnthropometricDataDto, HCEAnthropometricDataDto } from '@api-rest/api-model';
import { Observable } from 'rxjs';

@Component({
	selector: 'app-antropometricos-summary',
	templateUrl: './antropometricos-summary.component.html',
	styleUrls: ['./antropometricos-summary.component.scss']
})
export class AntropometricosSummaryComponent implements OnInit {

	@Input() internmentEpisodeId: number;
	@Input() anthropometricDataList$: Observable<HCEAnthropometricDataDto[]> | Observable<AnthropometricDataDto[]>;
	@Input() editable = false;

	details: DetailBoxExtended[] = [];
	readonly antropometricosSummary = ANTROPOMETRICOS;

	private readonly LABELS = {
		height: 'Talla (cm)',
		weight: 'Peso (kg)',
		bmi: 'IMC',
		headCircumference: 'Perímetro cefálico (cm)',
	};

	constructor(
		public dialog: MatDialog,
		private readonly internmentSummaryFacadeService: InternmentSummaryFacadeService,
	) { }

	ngOnInit(): void {
		this.anthropometricDataList$.subscribe(list => this.updateAnthropometricData(list));
	}

	openDialog(): void {
		const dialogRef = this.dialog.open(AddAnthropometricComponent, {
			disableClose: true,
			width: '25%',
			data: {
				internmentEpisodeId: this.internmentEpisodeId
			}
		});

		dialogRef.afterClosed().subscribe(fieldsToUpdate => {
			if (fieldsToUpdate) {
				this.internmentSummaryFacadeService.setFieldsToUpdate({ heightAndWeight: fieldsToUpdate.heightAndWeight,
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
		}
	}

}

interface DetailBoxExtended extends DetailBox {
	id: string;
}
