import { Injectable } from '@angular/core';
import { HolidaysService } from '@api-rest/services/holidays.service';
import { AppointmentsFacadeService } from '../../turnos/services/appointments-facade.service';
import { MatDialog } from '@angular/material/dialog';
import { DiscardWarningComponent } from '@presentation/dialogs/discard-warning/discard-warning.component';
import { HolidayDto } from '@api-rest/api-model';
import { Observable, map, of, switchMap } from 'rxjs';

@Injectable({
    providedIn: 'root'
})
export class HolidayCheckService {

    constructor(
        private readonly dialog: MatDialog,
		private readonly holidayService: HolidaysService,
		private readonly appointmentsFacade: AppointmentsFacadeService,
        ) { }

        checkAvailability(appointmentDate: string): Observable<boolean> {
            return this.holidayService.getHolidays(appointmentDate, appointmentDate).pipe(
                switchMap(holidays => {
                    return holidays.length ? this.isHoliday(holidays, appointmentDate) : of(true);
                })
            );
        }
        
        private isHoliday(holidays: HolidayDto[], appointmentDate: string): Observable<boolean> {
            let selectedDay = this.appointmentsFacade.checkIfHoliday(holidays, appointmentDate);
        
            if (!!selectedDay) {
                const dialogRef = this.dialog.open(DiscardWarningComponent, {
                    data: this.appointmentsFacade.getHolidayData(selectedDay)
                });
                return dialogRef.afterClosed().pipe(map(result => {
                    if (result || result == undefined) {
                        dialogRef?.close();
                        return false;
                    }
                    return true;
                }));
            }
            return of(true);
        }
}
