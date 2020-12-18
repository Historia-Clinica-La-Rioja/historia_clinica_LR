import { TestBed } from '@angular/core/testing';

import { PatientPhotoService } from './patient-photo.service';

describe('PatientPhotoService', () => {
  let service: PatientPhotoService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PatientPhotoService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
