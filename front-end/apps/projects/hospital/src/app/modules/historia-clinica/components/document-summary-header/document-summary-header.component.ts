import { Component, EventEmitter, Input, Output } from '@angular/core';
import { Position } from '@presentation/components/identifier/identifier.component';
import { IDENTIFIER_CASES } from '../../../hsi-components/identifier-cases/identifier-cases.component';

@Component({
    selector: 'app-document-summary-header',
    templateUrl: './document-summary-header.component.html',
    styleUrls: ['./document-summary-header.component.scss'],
})
export class DocumentSummaryHeaderComponent {

    @Input() headerData: HeaderData;
    @Input() isPopUpOpen: boolean;
    @Output() deleteDocument = new EventEmitter<boolean>();
    @Output() editDocument = new EventEmitter<boolean>();
    identiferCases = IDENTIFIER_CASES;
    position = Position;
    
    constructor() { }

    delete() {
        this.deleteDocument.emit(true);
    }

    edit() {
        this.editDocument.emit(true);
    }
}

export interface HeaderData {
    title: string,
    edit?: boolean,
    delete?: boolean,
    download?: boolean,
    headerDescriptionData?: HeaderDescriptionData,
}

export interface HeaderDescriptionData {
    scope?: string,
    patient?: string,
    specialty?: string,
    dateTime?: string,
    professional?: string,
    institution?: string,
    sector?: string,
    room?: string,
    bed?: string,
}