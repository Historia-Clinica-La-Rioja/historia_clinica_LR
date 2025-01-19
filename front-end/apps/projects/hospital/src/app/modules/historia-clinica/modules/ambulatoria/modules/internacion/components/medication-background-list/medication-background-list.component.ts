import { Component, Input, OnInit } from '@angular/core';
import { MedicationData, MedicationService } from '../../services/medicationService';

@Component({
    selector: 'app-medication-background-list',
    templateUrl: './medication-background-list.component.html',
    styleUrls: ['./medication-background-list.component.scss']
})
export class MedicationBackgroundListComponent implements OnInit {

    @Input() service: MedicationService;
    premedicationList: MedicationData[];

    constructor() { }

    ngOnInit(): void {
        this.service.getMedication().subscribe(premedicationList => {
            this.premedicationList = premedicationList;
        })
    }

}
