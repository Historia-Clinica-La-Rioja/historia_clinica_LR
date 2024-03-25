import { Component, Input, OnInit } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { AnestheticReportIntrasurgicalAnestheticProceduresService, INTRASURGICAL_ANESTHETIC_PROCEDURES_OPTIONS, IntresurgicalAnestheticProceduresRadioGroups } from '../../services/anesthetic-report-intrasurgical-anesthetic-procedures.service';
import { MatRadioChange } from '@angular/material/radio';

@Component({
    selector: 'app-anesthetic-report-intrasurgical-anesthetic-procedures',
    templateUrl: './anesthetic-report-intrasurgical-anesthetic-procedures.component.html',
    styleUrls: ['./anesthetic-report-intrasurgical-anesthetic-procedures.component.scss']
})
export class AnestheticReportIntrasurgicalAnestheticProceduresComponent implements OnInit {

    @Input() service: AnestheticReportIntrasurgicalAnestheticProceduresService;
    form: FormGroup;
    intrasurgicalAnestheticProceduresOptions = INTRASURGICAL_ANESTHETIC_PROCEDURES_OPTIONS;

    intrasurgicalAnestheticProceduresRadioGroups: IntresurgicalAnestheticProceduresRadioGroups;

    constructor() { }

    ngOnInit(): void {
        this.form = this.service.getForm();

        this.intrasurgicalAnestheticProceduresRadioGroups = this.service.getIntrasurgicalAnestheticProceduresRadioGroups();
    }

    onVenousAccessChange($event: MatRadioChange){
        this.service.setVenousAccess($event.value)
    }

    onNasogastricTubeChange($event: MatRadioChange){
        this.service.setNasogastricTube($event.value)
    }

    onUrinaryCatheterChange($event: MatRadioChange){
        this.service.setUrinaryCatheter($event.value)
    }
}