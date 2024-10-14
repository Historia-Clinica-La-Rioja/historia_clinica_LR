import { Component, Input } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { HCEPersonalHistoryDto } from '@api-rest/api-model';
import { fromStringToDateByDelimeter } from '@core/utils/date.utils';
import { PersonalHistoryViewDetailsComponent } from '@historia-clinica/modules/ambulatoria/dialogs/personal-history-view-details/personal-history-view-details.component';
import { SummaryHeader } from '@presentation/components/summary-card/summary-card.component';

@Component({
    selector: 'app-personal-histories-summary',
    templateUrl: './personal-histories-summary.component.html',
    styleUrls: ['./personal-histories-summary.component.scss']
})
export class PersonalHistoriesSummaryComponent {

    @Input() set personalHistories (personalHistories: HCEPersonalHistoryDto[]) {
        this.personalHistories_ = this.mapToPersonalHistoryData(personalHistories);
    }
    @Input() personalHistoriesHeader: SummaryHeader;
    personalHistories_: PersonalHistoryData[];
    
    
    constructor(
		private readonly dialog: MatDialog) { }

    private mapToPersonalHistoryData(data: HCEPersonalHistoryDto[]): PersonalHistoryData[] {
        return data.map((data: HCEPersonalHistoryDto) => {
            return {
                personalHistoryInfo: data,
                startDate: data.startDate ? fromStringToDateByDelimeter(data.startDate, '-') : null,
                inactivationDate: data.inactivationDate ? fromStringToDateByDelimeter(data.inactivationDate, '-') : null,
            };
        });
    }

    openViewDetailsDialog(personalHistory: PersonalHistoryData){
        this.dialog.open(PersonalHistoryViewDetailsComponent, {
			data: personalHistory,
			autoFocus: false,
			width: '40%',
		});
    }
}

export interface PersonalHistoryData {
    personalHistoryInfo: HCEPersonalHistoryDto,
    startDate: Date,
    inactivationDate: Date,
}