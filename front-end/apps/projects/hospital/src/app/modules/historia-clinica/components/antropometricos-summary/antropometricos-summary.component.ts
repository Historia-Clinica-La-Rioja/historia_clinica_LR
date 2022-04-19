import { Component, Input, OnInit } from '@angular/core';
import { ANTROPOMETRICOS } from '../../constants/summaries';
import { DetailBox } from '@presentation/components/detail-box/detail-box.component';
import { MatDialog } from '@angular/material/dialog';
import { AddAnthropometricComponent } from '../../dialogs/add-anthropometric/add-anthropometric.component';
import { Observable } from 'rxjs';
import { InternmentSummaryFacadeService } from "@historia-clinica/modules/ambulatoria/modules/internacion/services/internment-summary-facade.service";

@Component({
	selector: 'app-antropometricos-summary',
	templateUrl: './antropometricos-summary.component.html',
	styleUrls: ['./antropometricos-summary.component.scss']
})
export class AntropometricosSummaryComponent implements OnInit {

	@Input() internmentEpisodeId: number;
	@Input() anthropometricData$: Observable<any>;
	@Input() editable = false;
	@Input() hideBloodType = false;

	details: DetailBox[] = [];
	readonly antropometricosSummary = ANTROPOMETRICOS;

	private LABELS = {
		bloodType: 'Grupo sanguíneo',
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
		if (this.hideBloodType) {
			delete this.LABELS.bloodType;
		}
		this.updateAnthropometricData();
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
				this.updateAnthropometricData();
				this.internmentSummaryFacadeService.setFieldsToUpdate({ heightAndWeight: fieldsToUpdate.heightAndWeight, bloodType: fieldsToUpdate.bloodType });
			}
		}
		);
	}

	getIdentificator(name: DetailBox): string {
		return name.description.split(' (')[0]
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

	private updateAnthropometricData(): void {
		this.anthropometricData$.subscribe(
			anthropometricData => {
				if (anthropometricData) {
					if (anthropometricData.bmi?.value) {
						anthropometricData.bmi.value = this.truncateIfNecessary(anthropometricData.bmi?.value);
					}
					this.details = [];
					Object.keys(this.LABELS).forEach(
						key => {
							if (anthropometricData[key]?.value)
								this.details.push(
									{
										description: this.LABELS[key],
										value: anthropometricData[key].value
									}
								);
						}
					);
				}
			}
		);
	}

}
