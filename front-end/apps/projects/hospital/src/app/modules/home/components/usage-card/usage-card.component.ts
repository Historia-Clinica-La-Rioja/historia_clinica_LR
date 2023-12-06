import { Component, OnInit } from '@angular/core';
import { ApiErrorMessageDto, UsageReportStatusDto } from '@api-rest/api-model';
import { UsageReportService } from '@api-rest/services/usage-report.service';
import { Observable, catchError, of } from 'rxjs';

const ERROR_STATUS: UsageReportStatusDto = {
	domainId: '',
	isAllowedToSend: false,
};

@Component({
	selector: 'app-usage-card',
	templateUrl: './usage-card.component.html',
	styleUrls: ['./usage-card.component.scss']
})
export class UsageCardComponent implements OnInit {
	status$: Observable<UsageReportStatusDto>;
	apiError: ApiErrorMessageDto | undefined;

	constructor(
		public usageReportService: UsageReportService,
	) { }

	ngOnInit(): void {
		this.status$ = this.usageReportService.getStatus().pipe(
			catchError((error: ApiErrorMessageDto) => {
				this.apiError = error;
				return of(ERROR_STATUS);
			})
		);
	}

	sendReport(): void {
		this.status$ = undefined;
		this.usageReportService.sendReport().subscribe(
			_ => {
				this.ngOnInit();
			}
		);
	}

}
