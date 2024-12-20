import { Component, Input, OnInit } from '@angular/core';
import { ReferenceClosureSummaryDto, ReferenceCounterReferenceFileDto } from '@api-rest/api-model';
import { CounterreferenceFileService } from '@api-rest/services/counterreference-file.service';

@Component({
    selector: 'app-reference-closure',
    templateUrl: './reference-closure.component.html',
    styleUrls: ['./reference-closure.component.scss']
})
export class ReferenceClosureComponent implements OnInit {

    @Input() closure: ReferenceClosureSummaryDto;

    constructor(
        private readonly counterreferenceFileService: CounterreferenceFileService,
    ) { }

    ngOnInit(): void {
    }

    downloadFile(file: ReferenceCounterReferenceFileDto) {
		this.counterreferenceFileService.downloadCounterreferenceFiles(file.fileId, file.fileName);
	}

}
