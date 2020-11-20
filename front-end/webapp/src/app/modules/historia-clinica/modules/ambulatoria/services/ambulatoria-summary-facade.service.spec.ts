import { TestBed } from '@angular/core/testing';

import { AmbulatoriaSummaryFacadeService } from './ambulatoria-summary-facade.service';

describe('AmbulatoriaSummaryFacadeService', () => {
  let service: AmbulatoriaSummaryFacadeService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AmbulatoriaSummaryFacadeService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
