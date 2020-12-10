import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { NuevaPreinscripcionComponent } from './nueva-preinscripcion.component';

describe('NuevaPreinscripcionComponent', () => {
  let component: NuevaPreinscripcionComponent;
  let fixture: ComponentFixture<NuevaPreinscripcionComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ NuevaPreinscripcionComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(NuevaPreinscripcionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
