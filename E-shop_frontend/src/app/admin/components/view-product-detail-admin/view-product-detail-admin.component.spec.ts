import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ViewProductDetailAdminComponent } from './view-product-detail-admin.component';

describe('ViewProductDetailAdminComponent', () => {
  let component: ViewProductDetailAdminComponent;
  let fixture: ComponentFixture<ViewProductDetailAdminComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ViewProductDetailAdminComponent]
    });
    fixture = TestBed.createComponent(ViewProductDetailAdminComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
