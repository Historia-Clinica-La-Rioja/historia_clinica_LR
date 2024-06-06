import { Component, Input, OnInit } from '@angular/core';
import { isNumberOrDot } from '@core/utils/pattern.utils';
import { HceGeneralStateService } from '@api-rest/services/hce-general-state.service';
import { AnestheticReportAnthropometricDataService } from '../../services/anesthetic-report-anthropometric-data.service';
import { HCEAnthropometricDataDto } from '@api-rest/api-model';
import { FormGroup } from '@angular/forms';

@Component({
    selector: 'app-anesthetic-report-anthropometric-data',
    templateUrl: './anesthetic-report-anthropometric-data.component.html',
    styleUrls: ['./anesthetic-report-anthropometric-data.component.scss']
})
export class AnestheticReportAnthropometricDataComponent implements OnInit {
    
    @Input() patientId: number;
    @Input() service: AnestheticReportAnthropometricDataService;
	form: FormGroup;
    readonly isNumberOrDot = isNumberOrDot;

    constructor(
		private readonly hceGeneralStateService: HceGeneralStateService,
    ) { }

    ngOnInit(): void {
        this.setPreviousAnthropometricData();
		this.form = this.service.getForm();
    }

	setPreviousAnthropometricData(): void {
		if (this.patientId) {
			this.hceGeneralStateService.getAnthropometricData(this.patientId).subscribe(
				(anthropometricData: HCEAnthropometricDataDto) => {
					if (anthropometricData) {
						this.service.setAnthropometric(anthropometricData.weight?.value, anthropometricData.height?.value, anthropometricData.bloodType?.value);
					}
				}
			);
		}
	}
}
