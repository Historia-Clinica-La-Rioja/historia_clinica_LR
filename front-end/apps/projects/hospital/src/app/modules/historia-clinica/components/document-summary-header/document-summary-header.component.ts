import { Component, EventEmitter, Input, Output } from '@angular/core';
import { Position } from '@presentation/components/identifier/identifier.component';
import { IDENTIFIER_CASES } from '../../../hsi-components/identifier-cases/identifier-cases.component';
import { HeaderDescription, HeaderIdentifierData } from '@historia-clinica/utils/document-summary.model';
import { DocumentsSummaryMapperService } from '@historia-clinica/services/documents-summary-mapper.service';

@Component({
    selector: 'app-document-summary-header',
    templateUrl: './document-summary-header.component.html',
    styleUrls: ['./document-summary-header.component.scss'],
})
export class DocumentSummaryHeaderComponent {

    @Input() set headerData(headerData: HeaderDescription) {
        this._headerData = this.documentSummaryMapper.mapToHeaderIdentifierData(headerData);
    };
    @Input() isPopUpOpen: boolean;
    @Output() deleteDocument = new EventEmitter<boolean>();
    @Output() editDocument = new EventEmitter<boolean>();
    @Output() downloadDocument = new EventEmitter<boolean>();
    identiferCases = IDENTIFIER_CASES;
    position = Position;
    _headerData: HeaderIdentifierData;

    constructor(
        private readonly documentSummaryMapper: DocumentsSummaryMapperService
    ) { }

    delete() {
        this.deleteDocument.emit(true);
    }

    edit() {
        this.editDocument.emit(true);
    }

    download() {
        this.downloadDocument.emit(true);
    }
}