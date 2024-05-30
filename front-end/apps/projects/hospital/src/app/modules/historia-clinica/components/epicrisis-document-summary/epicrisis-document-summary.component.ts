import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { DocumentSearch } from '@historia-clinica/modules/ambulatoria/modules/internacion/services/document-actions.service';

@Component({
    selector: 'app-epicrisis-document-summary',
    templateUrl: './epicrisis-document-summary.component.html',
    styleUrls: ['./epicrisis-document-summary.component.scss']
})
export class EpicrisisDocumentSummaryComponent implements OnInit {

    @Input() isPopUpOpen: boolean;
    @Input() internmentEpisodeId: number;
    @Input() set activeDocument(activeDocument: DocumentSearch) {
        this._activeDocument = activeDocument;
    };
    @Output() resetActiveDocument = new EventEmitter<boolean>();
    
    _activeDocument: DocumentSearch;

    constructor() { }

    ngOnInit(): void {
    }

}
