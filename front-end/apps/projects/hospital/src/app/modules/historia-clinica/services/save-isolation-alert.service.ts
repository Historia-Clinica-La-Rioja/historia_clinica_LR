import { Injectable } from '@angular/core';
import { IsolationAlert } from '@historia-clinica/components/isolation-alert-form/isolation-alert-form.component';
import { Subject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class SaveIsolationAlertService {

  submitSubject = new Subject<void>();
  submit$ = this.submitSubject.asObservable();

  persistSubject = new Subject<IsolationAlert>();
  persist$ = this.persistSubject.asObservable();

  constructor() { }
}
