import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import { DatosPersonales } from '../../pacientes/pacientes.module';

@Injectable({
  providedIn: 'root'
})
export class SearchPatientService {

  constructor() { }

  getPatientByCMD(datosPersonales: DatosPersonales): Observable<any> {
    return of ([]);
    // TODO habilitar
  }
}

