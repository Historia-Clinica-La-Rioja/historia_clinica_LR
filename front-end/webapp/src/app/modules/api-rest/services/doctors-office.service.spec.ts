import { TestBed } from '@angular/core/testing';

import { DoctorsOfficeService } from './doctors-office.service';

describe('DoctorsOfficeService', () => {
  let service: DoctorsOfficeService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(DoctorsOfficeService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
