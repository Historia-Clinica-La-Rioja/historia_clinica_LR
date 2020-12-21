import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AdmisionAdministrativaComponent } from './admision-administrativa.component';

describe('AdmisionAdministrativaComponent', () => {
  let component: AdmisionAdministrativaComponent;
  let fixture: ComponentFixture<AdmisionAdministrativaComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AdmisionAdministrativaComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AdmisionAdministrativaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
