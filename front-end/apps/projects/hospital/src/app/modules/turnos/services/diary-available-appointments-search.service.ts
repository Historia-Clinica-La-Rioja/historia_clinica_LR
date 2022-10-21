import { Injectable } from '@angular/core';
import { DiaryAvailableProtectedAppointmentsDto } from '@api-rest/api-model';
import { Observable, of } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class DiaryAvailableAppointmentsSearchService {

  constructor() { }

  getAvailableProtectedAppointments(institutionId: number, filters: ProtectedAppointmentsFilter): Observable<DiaryAvailableProtectedAppointmentsDto[]> {
    return of(
      [
        {
          clinicalSpecialty: { id: 5, name: "Ginecología" },
          date: {
            day: 5,
            month: 11,
            year: 2022,
          },

          department: { id: 14, description: "Bolivar" },
          diaryId: 511,
          doctorsOffice: "oficina1",
          hour: {
            hours: 14,
            minutes: 30,
          },
          institution: {
            id: 1,
            name: "clinica chacabuco",
          },
          jointDiary: true,
          professionalFullName: "Marcelo Tinelli"

        },
        {
          clinicalSpecialty: { id: 5, name: "Pediatría" },
          date: {
            day: 5,
            month: 11,
            year: 2022,
          },

          department: { id: 14, description: "Tandil" },
          diaryId: 511,
          doctorsOffice: "oficina1",
          hour: {
            hours: 14,
            minutes: 30,
          },
          institution: {
            id: 1,
            name: "clinica Paz",
          },
          jointDiary: true,
          professionalFullName: "Jorgelin Tinelli"

        }
      ]
    );
  }
}

export interface ProtectedAppointmentsFilter {
  careLineId?: number,
  clinicalSpecialtyId: number,
  departmentId: number,
  initialSearchDate: {
    year: number,
    month: number,
    day: number
  },
  endSearchDate: {
    year: number,
    month: number,
    day: number
  }
}
