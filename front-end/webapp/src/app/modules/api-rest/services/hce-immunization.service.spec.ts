import { TestBed } from '@angular/core/testing';
import { HceImmunizationService } from './hce-immunization.service';


describe('HceImmunizationService', () => {
  let service: HceImmunizationService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(HceImmunizationService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
