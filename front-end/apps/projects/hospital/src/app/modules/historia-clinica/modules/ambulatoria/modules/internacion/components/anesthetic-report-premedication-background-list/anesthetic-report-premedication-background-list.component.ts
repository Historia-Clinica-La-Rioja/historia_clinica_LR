import { Component, Input, OnInit } from '@angular/core';
import { AnestheticReportPremedicationAndFoodIntakeService, PremedicationAndFoodIntakeData } from '../../services/anesthetic-report-premedication-and-food-intake.service';

@Component({
    selector: 'app-anesthetic-report-premedication-background-list',
    templateUrl: './anesthetic-report-premedication-background-list.component.html',
    styleUrls: ['./anesthetic-report-premedication-background-list.component.scss']
})
export class AnestheticReportPremedicationBackgroundListComponent implements OnInit {

    @Input() service: AnestheticReportPremedicationAndFoodIntakeService;
    premedicationList: PremedicationAndFoodIntakeData[];

    constructor() { }

    ngOnInit(): void {
        this.service.getPremedication().subscribe(premedicationList => {
            this.premedicationList = premedicationList;
        })
    }

}
