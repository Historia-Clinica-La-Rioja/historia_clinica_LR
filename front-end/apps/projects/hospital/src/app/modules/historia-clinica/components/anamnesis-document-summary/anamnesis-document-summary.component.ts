import { Component, Input, OnInit } from '@angular/core';
import { ResponseAnamnesisDto } from '@api-rest/api-model';
import { AnamnesisService } from '@api-rest/services/anamnesis.service';
import { AnamnesisAsViewFormat, AnamnesisDocumentSummaryService } from '@historia-clinica/services/anamnesis-document-summary.service';
import { Observable, tap } from 'rxjs';

@Component({
    selector: 'app-anamnesis-document-summary',
    templateUrl: './anamnesis-document-summary.component.html',
    styleUrls: ['./anamnesis-document-summary.component.scss']
})
export class AnamnesisDocumentSummaryComponent implements OnInit {

    @Input() isPopUpOpen: boolean;
    @Input() internmentEpisodeId: number;
    @Input() set documentId (documentId: number) {
        this._documentId = documentId;
        this.anamnesis = null;
        if (this.internmentEpisodeId && this._documentId) {
            this.anamnesis$ = this.anamnesisService.getAnamnesis(this._documentId, this.internmentEpisodeId).pipe(tap(anamnesis => {
                this.anamnesis = this.anamnesisDocumentSummaryService.getAnamnesisAsViewFormat(anamnesis);
                this.isLoading = false;
            } ))
        }
    };

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

    
    anamnesis: AnamnesisAsViewFormat;
    _documentId: number
    anamnesis$: Observable<ResponseAnamnesisDto>;
    isLoading = true;
    constructor(
        private readonly anamnesisService: AnamnesisService,
        private readonly anamnesisDocumentSummaryService: AnamnesisDocumentSummaryService,
    ) { }

    ngOnInit(): void {
        this.anamnesis$ = this.anamnesisService.getAnamnesis(this._documentId, this.internmentEpisodeId).pipe(tap(anamnesis => {
            this.anamnesis = this.anamnesisDocumentSummaryService.getAnamnesisAsViewFormat(anamnesis);
            this.isLoading = false;
        } ))
    }
}
