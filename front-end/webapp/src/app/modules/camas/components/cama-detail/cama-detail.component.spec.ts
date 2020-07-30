import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CamaDetailComponent } from './cama-detail.component';

describe('CamaDetailComponent', () => {
  let component: CamaDetailComponent;
  let fixture: ComponentFixture<CamaDetailComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CamaDetailComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CamaDetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
