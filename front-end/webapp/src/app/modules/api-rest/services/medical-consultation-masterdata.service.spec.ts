import { TestBed } from '@angular/core/testing';
import { MedicalConsultationMasterdataService } from './medical-consultation-masterdata.service';


describe('MedicalConsultationMasterdataService', () => {
  let service: MedicalConsultationMasterdataService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(MedicalConsultationMasterdataService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
