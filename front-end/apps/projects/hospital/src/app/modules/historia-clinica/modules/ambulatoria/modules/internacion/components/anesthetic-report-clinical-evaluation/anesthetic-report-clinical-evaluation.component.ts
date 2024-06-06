import { Component, Input, OnInit } from '@angular/core';
import { AnestheticReportClinicalEvaluationService } from '../../services/anesthetic-report-clinical-evaluation.service';
import { FormGroup } from '@angular/forms';

@Component({
    selector: 'app-anesthetic-report-clinical-evaluation',
    templateUrl: './anesthetic-report-clinical-evaluation.component.html',
    styleUrls: ['./anesthetic-report-clinical-evaluation.component.scss']
})
export class AnestheticReportClinicalEvaluationComponent implements OnInit {

    @Input() service: AnestheticReportClinicalEvaluationService;
    form: FormGroup;

    constructor( ) {}

    ngOnInit(): void {
        this.form = this.service.getForm();
    }

}
