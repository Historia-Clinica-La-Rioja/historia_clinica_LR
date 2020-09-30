import { TestBed } from '@angular/core/testing';

import { DailyAppointmentService } from './daily-appointment.service';

describe('DailyAppointmentService', () => {
  let service: DailyAppointmentService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(DailyAppointmentService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
