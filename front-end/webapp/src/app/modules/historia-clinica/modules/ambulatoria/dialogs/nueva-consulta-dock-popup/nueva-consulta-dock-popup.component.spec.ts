import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { NuevaConsultaDockPopupComponent } from './nueva-consulta-dock-popup.component';

describe('NuevaConsultaDockPopupComponent', () => {
  let component: NuevaConsultaDockPopupComponent;
  let fixture: ComponentFixture<NuevaConsultaDockPopupComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ NuevaConsultaDockPopupComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(NuevaConsultaDockPopupComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
