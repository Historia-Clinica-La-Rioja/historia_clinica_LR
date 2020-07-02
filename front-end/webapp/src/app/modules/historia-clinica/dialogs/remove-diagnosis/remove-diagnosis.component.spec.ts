import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { RemoveDiagnosisComponent } from './remove-diagnosis.component';

describe('RemoveDiagnosisComponent', () => {
  let component: RemoveDiagnosisComponent;
  let fixture: ComponentFixture<RemoveDiagnosisComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ RemoveDiagnosisComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RemoveDiagnosisComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
