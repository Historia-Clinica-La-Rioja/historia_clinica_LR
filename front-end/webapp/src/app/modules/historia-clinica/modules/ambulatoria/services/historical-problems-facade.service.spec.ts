import { TestBed } from '@angular/core/testing';

import { HistoricalProblemsFacadeService } from './historical-problems-facade.service';

describe('HistoricalProblemsFacadeService', () => {
  let service: HistoricalProblemsFacadeService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(HistoricalProblemsFacadeService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
