import { Component, Input, OnInit } from '@angular/core';
import { TerminologyQueueItemDto } from '@api-rest/api-model';
import { LoadStatus, SnomedCacheService } from '../snomed-cache/snomed-cache.service';
import { DatePipeFormat } from '@core/utils/date.utils';
import { TERMINOLOGYLOADSTATUS } from '../snomed-cache/snomed-cache.component';

@Component({
    selector: 'app-snomed-terminology-card',
    templateUrl: './snomed-terminology-card.component.html',
    styleUrls: ['./snomed-terminology-card.component.scss']
})
export class SnomedTerminologyCardComponent implements OnInit {

    @Input() uploadedTerminology: TerminologyQueueItemDto;
    @Input() successful: boolean;
    readonly dateFormats = DatePipeFormat;
    uploadedTerminologyData: TerminologyQueueItemData;
    terminologyLoadStatus = TERMINOLOGYLOADSTATUS;

    constructor(private snomedCacheService: SnomedCacheService) { }

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

    delete(terminologyId: number) {
        this.snomedCacheService.delete(terminologyId);
    }
}

export interface TerminologyQueueItemData {
    data: TerminologyQueueItemDto,
    loadStatus: LoadStatus;
}
