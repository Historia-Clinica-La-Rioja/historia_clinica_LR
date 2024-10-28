import { Component, Input, OnInit } from '@angular/core';
import { ReferenceClosureSummaryDto, ReferenceCounterReferenceFileDto } from '@api-rest/api-model';
import { ReferenceFileService } from '@api-rest/services/reference-file.service';

@Component({
    selector: 'app-reference-closure',
    templateUrl: './reference-closure.component.html',
    styleUrls: ['./reference-closure.component.scss']
})
export class ReferenceClosureComponent implements OnInit {

    @Input() closure: ReferenceClosureSummaryDto;

    constructor(
        private readonly referenceFileService: ReferenceFileService,
    ) { }

    ngOnInit(): void {
    }

    downloadFile(file: ReferenceCounterReferenceFileDto) {
		this.referenceFileService.downloadReferenceFiles(file.fileId, file.fileName);
	}

}
