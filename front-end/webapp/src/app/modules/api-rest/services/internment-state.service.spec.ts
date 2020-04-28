import { TestBed } from '@angular/core/testing';

import { InternmentStateService } from './internment-state.service';

describe('InternmentStateService', () => {
  let service: InternmentStateService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(InternmentStateService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
