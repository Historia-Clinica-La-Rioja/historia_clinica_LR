import { TestBed } from '@angular/core/testing';

import { OdontologyService } from './odontology.service';
import { HttpClientModule } from '@angular/common/http';

describe('OdontologyService', () => {
  let service: OdontologyService;

  beforeEach(() => {
	  TestBed.configureTestingModule({
		  imports: [HttpClientModule]
	  });
	service = TestBed.inject(OdontologyService);
  });

  it('should be created', () => {
	expect(service).toBeTruthy();
  });
});
