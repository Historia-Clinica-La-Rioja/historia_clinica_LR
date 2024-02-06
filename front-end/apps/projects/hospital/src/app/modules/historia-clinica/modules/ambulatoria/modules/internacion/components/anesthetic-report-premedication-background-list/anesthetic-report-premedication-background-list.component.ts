import { Component, Input, OnInit } from '@angular/core';
import { MedicationData, MedicationService } from '../../services/medicationService';

@Component({
    selector: 'app-anesthetic-report-premedication-background-list',
    templateUrl: './anesthetic-report-premedication-background-list.component.html',
    styleUrls: ['./anesthetic-report-premedication-background-list.component.scss']
})
export class AnestheticReportPremedicationBackgroundListComponent implements OnInit {

    @Input() service: MedicationService;
    premedicationList: MedicationData[];

    constructor() { }

    ngOnInit(): void {
        this.service.getMedication().subscribe(premedicationList => {
            this.premedicationList = premedicationList;
        })
    }

}
