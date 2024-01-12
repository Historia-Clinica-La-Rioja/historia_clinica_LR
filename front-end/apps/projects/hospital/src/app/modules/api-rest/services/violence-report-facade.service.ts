import { Injectable } from '@angular/core';
import { PageDto, ViolenceReportSituationDto } from '@api-rest/api-model';
import { BehaviorSubject } from 'rxjs';
import { ViolenceReportService } from './violence-report.service';

@Injectable({
    providedIn: 'root'
})
export class ViolenceReportFacadeService {

    violenceSituations$ = new BehaviorSubject<PageDto<ViolenceReportSituationDto>>(null);

    constructor(private readonly violenceReportService: ViolenceReportService) {}

    setAllPatientViolenceSituations(patientId: number, mustBeLimited: boolean) {
        this.violenceSituations$.next(null);
        this.violenceReportService.getLimitedPatientViolenceSituations(patientId, mustBeLimited)
            .subscribe((result: PageDto<ViolenceReportSituationDto>) => this.violenceSituations$.next(result));
    }
}
