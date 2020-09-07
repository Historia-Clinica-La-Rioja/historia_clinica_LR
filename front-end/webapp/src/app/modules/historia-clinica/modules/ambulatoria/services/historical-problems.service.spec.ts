import { TestBed } from '@angular/core/testing';

import { HistoricalProblemsService } from './historical-problems.service';

describe('HistoricalProblemsService', () => {
  let service: HistoricalProblemsService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(HistoricalProblemsService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
