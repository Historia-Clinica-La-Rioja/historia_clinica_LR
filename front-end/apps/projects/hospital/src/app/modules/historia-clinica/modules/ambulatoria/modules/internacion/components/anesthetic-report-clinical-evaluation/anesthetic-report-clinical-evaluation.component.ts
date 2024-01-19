import { Component, OnInit } from '@angular/core';
import { AnestheticReportClinicalEvaluationService } from '../../services/anesthetic-report-clinical-evaluation.service';
import { TranslateService } from '@ngx-translate/core';
import { FormGroup } from '@angular/forms';

@Component({
    selector: 'app-anesthetic-report-clinical-evaluation',
    templateUrl: './anesthetic-report-clinical-evaluation.component.html',
    styleUrls: ['./anesthetic-report-clinical-evaluation.component.scss']
})
export class AnestheticReportClinicalEvaluationComponent implements OnInit {

    form: FormGroup;
    anestheticReportClinicalEvaluationService: AnestheticReportClinicalEvaluationService;

    constructor(
        /* private readonly internacionMasterDataService: InternacionMasterDataService,*/
        private readonly translateService: TranslateService,

    ) {
        this.anestheticReportClinicalEvaluationService = new AnestheticReportClinicalEvaluationService(this.translateService);
    }

    ngOnInit(): void {
        this.form = this.anestheticReportClinicalEvaluationService.getForm();
    }

}
