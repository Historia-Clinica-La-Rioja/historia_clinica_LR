import { Component, Input, OnInit } from '@angular/core';
import { isNumberOrDot } from '@core/utils/pattern.utils';
import { HceGeneralStateService } from '@api-rest/services/hce-general-state.service';
import { HCEAnthropometricDataDto } from '@api-rest/api-model';
import { FormGroup } from '@angular/forms';
import { AnestheticReportService } from '../../services/anesthetic-report.service';

@Component({
    selector: 'app-anesthetic-report-anthropometric-data',
    templateUrl: './anesthetic-report-anthropometric-data.component.html',
    styleUrls: ['./anesthetic-report-anthropometric-data.component.scss']
})
export class AnestheticReportAnthropometricDataComponent implements OnInit {
    
    @Input() patientId: number;
	form: FormGroup;
    readonly isNumberOrDot = isNumberOrDot;

    constructor(
		private readonly hceGeneralStateService: HceGeneralStateService,
		readonly service: AnestheticReportService,
    ) { }

    ngOnInit(): void {
        this.setPreviousAnthropometricData();
		this.form = this.service.anesthesicReportAnthropometricDataService.getForm();
    }

	setPreviousAnthropometricData(): void {
		if (this.patientId) {
			this.hceGeneralStateService.getAnthropometricData(this.patientId).subscribe(
				(anthropometricData: HCEAnthropometricDataDto) => {
					if (anthropometricData) {
						this.service.anesthesicReportAnthropometricDataService.setAnthropometric(anthropometricData.weight?.value, anthropometricData.height?.value, anthropometricData.bloodType?.value);
					}
				}
			);
		}
	}
}
