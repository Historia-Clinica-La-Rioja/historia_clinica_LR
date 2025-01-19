import { Component, OnInit } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { AnestheticReportService } from '../../services/anesthetic-report.service';

@Component({
    selector: 'app-anesthetic-report-clinical-evaluation',
    templateUrl: './anesthetic-report-clinical-evaluation.component.html',
    styleUrls: ['./anesthetic-report-clinical-evaluation.component.scss']
})
export class AnestheticReportClinicalEvaluationComponent implements OnInit {

    form: FormGroup;

    constructor(
        readonly service: AnestheticReportService,
    ) {}

    ngOnInit(): void {
        this.form = this.service.anestheticReportClinicalEvaluationService.getForm();
    }

}
