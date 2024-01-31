import { Injectable } from '@angular/core';
import { PageDto, ViolenceReportDto, ViolenceReportSituationDto, ViolenceReportSituationEvolutionDto } from '@api-rest/api-model';
import { BehaviorSubject, Subject } from 'rxjs';
import { ViolenceReportService } from './violence-report.service';

@Injectable({
    providedIn: 'root'
})
export class ViolenceReportFacadeService {

    violenceSituations$ = new BehaviorSubject<PageDto<ViolenceReportSituationDto>>(null);
    violenceSituation$ = new Subject<ViolenceReportDto>();
    evolutions$ = new Subject<ViolenceReportSituationEvolutionDto[]>();

    constructor(private readonly violenceReportService: ViolenceReportService) {}

    setAllPatientViolenceSituations(patientId: number, mustBeLimited: boolean) {
        this.violenceSituations$.next(null);
        this.violenceReportService.getLimitedPatientViolenceSituations(patientId, mustBeLimited)
            .subscribe((result: PageDto<ViolenceReportSituationDto>) => this.violenceSituations$.next(result));
    }

    setViolenceSituation(situationId: number, patientId: number) {
        this.violenceReportService.getViolenceReport(situationId, patientId)
            .subscribe((result: ViolenceReportDto) => this.violenceSituation$.next(result));
    }

    setEvolutions = (patientId: number, filterData: string) => {
        this.violenceReportService.getEvolutions(patientId, filterData)
			.subscribe({
				next: (result: ViolenceReportSituationEvolutionDto[]) => this.evolutions$.next(result)
			});
    }
}
