import { TestBed } from '@angular/core/testing';

import { AppointmentsFacadeService } from './appointments-facade.service';

describe('AppointmentsFacadeService', () => {
  let service: AppointmentsFacadeService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AppointmentsFacadeService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
