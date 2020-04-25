import { TestBed } from '@angular/core/testing';

import { AnamnesisFormService } from './anamnesis-form.service';

describe('AnamnesisFormService', () => {
  let service: AnamnesisFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AnamnesisFormService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
