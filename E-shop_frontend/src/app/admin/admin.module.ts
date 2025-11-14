import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatButtonModule } from '@angular/material/button';
import { MatToolbarModule } from '@angular/material/toolbar';
import { AdminRoutingModule } from './admin-routing.module';
import { AdminComponent } from './admin.component';
import { DashboardComponent } from './components/dashboard/dashboard.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { PostCategoryComponent } from './components/post-category/post-category.component';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { PostProductComponent } from './components/post-product/post-product.component';
import { MatSelectModule } from '@angular/material/select';
import { MatDividerModule } from '@angular/material/divider';
import { PostCouponComponent } from './components/post-coupon/post-coupon.component';
import { CouponsComponent } from './components/coupons/coupons.component';
import { MatDatepickerModule } from '@angular/material/datepicker';

import { MatNativeDateModule } from '@angular/material/core'; 
import { MatTableModule } from '@angular/material/table';
import { OrdersComponent } from './components/orders/orders.component';
import { MatMenuModule } from '@angular/material/menu';
import { PostProductFaqComponent } from './components/post-product-faq/post-product-faq.component';
import { UpdateProductComponent } from './components/update-product/update-product.component';
import { AnalyticsComponent } from './components/analytics/analytics.component';
import { OrderByStatusComponent } from './components/analytics/order-by-status/order-by-status.component';
import { MatPaginatorModule } from '@angular/material/paginator';
import { ViewProductDetailAdminComponent } from './components/view-product-detail-admin/view-product-detail-admin.component';








@NgModule({
  declarations: [
    AdminComponent,
    DashboardComponent,
    PostCategoryComponent,
    PostProductComponent,
    PostCouponComponent,
    CouponsComponent,
    OrdersComponent,
    PostProductFaqComponent,
    UpdateProductComponent,
    AnalyticsComponent,
    OrderByStatusComponent,
    ViewProductDetailAdminComponent
  ],
  imports: [
    CommonModule,
    AdminRoutingModule,
    FormsModule,
    ReactiveFormsModule,
    HttpClientModule,
    MatFormFieldModule,
    MatInputModule,
    MatCardModule,
    MatIconModule,
    MatSnackBarModule,
    MatToolbarModule,
    MatButtonModule,
    MatSelectModule,
    MatDividerModule,
    MatDatepickerModule,
    MatNativeDateModule,
    MatTableModule,
    MatMenuModule,
    MatButtonModule,
    MatPaginatorModule,
    

  ]
})
export class AdminModule { }
