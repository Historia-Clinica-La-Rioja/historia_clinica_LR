import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { TerminologyQueueItemDto } from '@api-rest/api-model';
import { LoadStatus } from '../snomed-cache/snomed-cache.service';
import { TERMINOLOGYLOADSTATUS } from '../snomed-cache/snomed-cache.component';

@Component({
    selector: 'app-snomed-terminology-card',
    templateUrl: './snomed-terminology-card.component.html',
    styleUrls: ['./snomed-terminology-card.component.scss']
})
export class SnomedTerminologyCardComponent implements OnInit {
    @Input() uploadedTerminology: TerminologyQueueItemDto;
    @Input() successful: boolean;
	@Output() onDelete = new EventEmitter<void>();


    uploadedTerminologyData: TerminologyQueueItemData;
    terminologyLoadStatus = TERMINOLOGYLOADSTATUS;

    constructor() { }

    ngOnInit(): void {
    }

    ngOnChanges() {
        if (this.uploadedTerminology) {
            this.uploadedTerminologyData = this.mapToTerminologyQueueItemData(this.uploadedTerminology);
        }
    }

    private mapToTerminologyQueueItemData(uploadedTerminology: TerminologyQueueItemDto): TerminologyQueueItemData{
        return {
            data: uploadedTerminology,
            loadStatus: this.getStatus(uploadedTerminology)
        }
    }

    private getStatus(uploadedTerminology: TerminologyQueueItemDto): LoadStatus {
        if (this.successful) return TERMINOLOGYLOADSTATUS.LOADED;
        if ((uploadedTerminology.createdOn && !uploadedTerminology.ingestedOn) && !uploadedTerminology.downloadedError) return TERMINOLOGYLOADSTATUS.PENDING;
		return TERMINOLOGYLOADSTATUS.NOT_LOADED;
    }
}

export interface TerminologyQueueItemData {
    data: TerminologyQueueItemDto,
    loadStatus: LoadStatus;
}
