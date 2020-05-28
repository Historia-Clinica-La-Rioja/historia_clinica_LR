import { TestBed } from '@angular/core/testing';

import { FeatureFlagServiceService } from './feature-flag.service';

describe('FeatureFlagServiceService', () => {
  let service: FeatureFlagServiceService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(FeatureFlagServiceService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
