import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class EquipmentService {

  constructor() { }

  getBySectorId(sectorId: number): Observable<any[]> {
    return of([{id: 1, name: "equipo de prueba"}]);
  }
}
