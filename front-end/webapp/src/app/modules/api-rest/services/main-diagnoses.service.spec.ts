import { TestBed } from '@angular/core/testing';

import { MainDiagnosesService } from './main-diagnoses.service';

describe('MainDiagnosesService', () => {
  let service: MainDiagnosesService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(MainDiagnosesService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
