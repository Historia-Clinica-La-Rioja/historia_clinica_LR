import { TestBed } from '@angular/core/testing';

import { DiaryOpeningHoursService } from './diary-opening-hours.service';

describe('DiaryOpeningHoursService', () => {
  let service: DiaryOpeningHoursService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(DiaryOpeningHoursService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
