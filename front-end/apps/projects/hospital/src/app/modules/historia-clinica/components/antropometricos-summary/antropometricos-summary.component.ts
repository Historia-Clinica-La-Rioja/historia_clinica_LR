import { Component, Input, OnInit } from '@angular/core';
import { ANTROPOMETRICOS } from '../../constants/summaries';
import { DetailBox } from '@presentation/components/detail-box/detail-box.component';
import { AnthropometricDataDto } from '@api-rest/api-model';
import { MatDialog } from '@angular/material/dialog';
import { AddAnthropometricComponent } from '../../dialogs/add-anthropometric/add-anthropometric.component';
import { Observable } from 'rxjs';

@Component({
	selector: 'app-antropometricos-summary',
	templateUrl: './antropometricos-summary.component.html',
	styleUrls: ['./antropometricos-summary.component.scss']
})
export class AntropometricosSummaryComponent implements OnInit {

	@Input() internmentEpisodeId: number;
	@Input() anthropometricData$: Observable<AnthropometricDataDto>;
	@Input() editable = false;

	antropometricosSummary = ANTROPOMETRICOS;

	details: DetailBox[] = [];

	private readonly LABELS = {
		bloodType: 'Grupo sanguíneo',
		height: 'Talla (cm)',
		weight: 'Peso (kg)',
		bmi: 'IMC',
		headCircumference: 'Perímetro cefálico (cm)',
	};

	constructor(
		public dialog: MatDialog
	) { }

	ngOnInit(): void {
		this.updateAnthropometricData();
	}

	private updateAnthropometricData() {
		this.anthropometricData$.subscribe(
			(anthropometricData: AnthropometricDataDto) => {
				if (anthropometricData) {
					const BMI = this.calculateBMI(anthropometricData.height?.value, anthropometricData.weight?.value);
					if (BMI) {
						anthropometricData.bmi = { value: BMI };
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

	openDialog() {
		const dialogRef = this.dialog.open(AddAnthropometricComponent, {
			disableClose: true,
			width: '25%',
			data: {
				internmentEpisodeId: this.internmentEpisodeId
			}
		});

		dialogRef.afterClosed().subscribe(submitted => {
			if (submitted) {
				this.updateAnthropometricData();
			}
		}
		);
	}

	private calculateBMI(height: string, weight: string): string {
		if (!height || !weight) {
			return undefined;
		}

		const heightNumber = parseInt(height, 10) / 100;
		const weightNumber = parseInt(weight, 10);

		if (heightNumber > 0 && weightNumber > 0) {
			const result = Math.round((weightNumber / (heightNumber * heightNumber)) * 10) / 10;
			return result.toString(10);
		}

		return undefined;
	}

}
