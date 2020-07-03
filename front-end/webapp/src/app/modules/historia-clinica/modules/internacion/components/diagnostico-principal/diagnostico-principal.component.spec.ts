import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DiagnosticoPrincipalComponent } from './diagnostico-principal.component';

describe('DiagnosticoPrincipalComponent', () => {
  let component: DiagnosticoPrincipalComponent;
  let fixture: ComponentFixture<DiagnosticoPrincipalComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DiagnosticoPrincipalComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DiagnosticoPrincipalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
