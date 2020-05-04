import { TestBed } from '@angular/core/testing';

import { AnamnesisService } from './anamnesis.service';

describe('AnamnesisService', () => {
  let service: AnamnesisService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AnamnesisService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
