
import { Component } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ActivatedRoute } from '@angular/router';
import { AdminService } from '../../service/admin.service';

import { UserStorageService } from 'src/app/services/storage/user-storage.service';

@Component({
  selector: 'app-view-product-detail-admin',
  templateUrl: './view-product-detail-admin.component.html',
  styleUrls: ['./view-product-detail-admin.component.css']
})
export class ViewProductDetailAdminComponent {
  productId: number = this.activatedRoute.snapshot.params["productId"];
  product: any;
  FAQS: any[] = [];
  reviews: any[] = [];
  isInWishlist: boolean = false; // ✅ Add this line

  constructor(
    private adminService: AdminService,
    private snackBar: MatSnackBar,
    private activatedRoute: ActivatedRoute
  ) {}

  averageRating: number = 0;


  ngOnInit() {
  this.getProductDetailById();
}




getProductDetailById() {
  this.adminService.getProductDetailById(this.productId).subscribe(res => {
    this.product = res.productDto;

    // Image principale
    if (this.product.imageUrl) {
      this.product.processedImg = this.product.imageUrl;
    } else if (this.product.byteImg) {
      this.product.processedImg = 'data:image/png;base64,' + this.product.byteImg;
    }

    this.FAQS = res.faqDtoList || [];
    this.reviews = (res.reviewDtoList || []).map(element => {
      if (element.imageUrl) {
        element.processedImg = element.imageUrl;
      } else if (element.returnedImg) {
        element.processedImg = 'data:image/png;base64,' + element.returnedImg;
      }
      return element;
    });

    // ✅ Calcul moyenne ici
    this.averageRating = this.reviews.length > 0
      ? this.reviews.reduce((acc, r) => acc + (r.rating || 0), 0) / this.reviews.length
      : 0;
  });
}


  
}
