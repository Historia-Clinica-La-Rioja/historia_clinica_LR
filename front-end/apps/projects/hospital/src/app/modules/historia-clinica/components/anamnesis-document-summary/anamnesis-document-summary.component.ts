import { Component, Input, OnInit } from '@angular/core';

@Component({
    selector: 'app-anamnesis-document-summary',
    templateUrl: './anamnesis-document-summary.component.html',
    styleUrls: ['./anamnesis-document-summary.component.scss']
})
export class AnamnesisDocumentSummaryComponent implements OnInit {

    @Input() isPopUpOpen: boolean;

    headerTestInfo = {
        title: "Evaluación de ingreso",
        edit: true,
        delete: true,
        download: true,
        headerDescriptionData: {
            scope: "Internación",
            specialty: "Traumatología",
            dateTime: "09/12/2018 - 23:00hs",
            professional: "Nombre prof",
            institution: "Nombre inst",
            sector: "Nombre sect",
            room: "Nombre sala",
            bed: "Nombre cama",
        },
    }
    constructor() { }

    ngOnInit(): void {
    }

}
