import { Component, Input, OnInit } from '@angular/core';
import { isNumberOrDot } from '@core/utils/pattern.utils';
import { TranslateService } from '@ngx-translate/core';
import { HceGeneralStateService } from '@api-rest/services/hce-general-state.service';
import { InternacionMasterDataService } from '@api-rest/services/internacion-master-data.service';
import { AnestheticReportAnthropometricDataService } from '../../services/anesthetic-report-anthropometric-data.service';
import { HCEAnthropometricDataDto } from '@api-rest/api-model';

@Component({
    selector: 'app-anesthetic-report-anthropometric-data',
    templateUrl: './anesthetic-report-anthropometric-data.component.html',
    styleUrls: ['./anesthetic-report-anthropometric-data.component.scss']
})
export class AnestheticReportAnthropometricDataComponent implements OnInit {
    
    @Input() patientId: number;
    anthropometricDataService: AnestheticReportAnthropometricDataService;
    readonly isNumberOrDot = isNumberOrDot;

    constructor(
		private readonly internacionMasterDataService: InternacionMasterDataService,
		private readonly translateService: TranslateService,
		private readonly hceGeneralStateService: HceGeneralStateService,
        
    ) {
        this.anthropometricDataService = new AnestheticReportAnthropometricDataService(this.internacionMasterDataService, this.translateService);
    }

    ngOnInit(): void {
        this.setPreviousAnthropometricData();
    }

	setPreviousAnthropometricData(): void {
		if (this.patientId) {
			this.hceGeneralStateService.getAnthropometricData(this.patientId).subscribe(
				(anthropometricData: HCEAnthropometricDataDto) => {
					if (anthropometricData) {
						this.anthropometricDataService.setAnthropometric(anthropometricData.weight?.value, anthropometricData.height?.value, anthropometricData.bloodType?.value);
					}
				}
			);
		}
	}
}
