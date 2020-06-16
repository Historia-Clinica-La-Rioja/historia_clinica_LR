import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CambiarDiagnosticoPrincipalComponent } from './cambiar-diagnostico-principal.component';

describe('CambiarDiagnosticoPrincipalComponent', () => {
  let component: CambiarDiagnosticoPrincipalComponent;
  let fixture: ComponentFixture<CambiarDiagnosticoPrincipalComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CambiarDiagnosticoPrincipalComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CambiarDiagnosticoPrincipalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
