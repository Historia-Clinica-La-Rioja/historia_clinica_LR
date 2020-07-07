import { TestBed } from '@angular/core/testing';

import { HceGeneralStateService } from './hce-general-state.service';

describe('HceGeneralStateService', () => {
  let service: HceGeneralStateService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(HceGeneralStateService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
