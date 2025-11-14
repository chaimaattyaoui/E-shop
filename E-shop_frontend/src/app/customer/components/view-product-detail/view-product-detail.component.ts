import { Component } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ActivatedRoute } from '@angular/router';
import { CustomerService } from '../../services/customer.service';
import { UserStorageService } from 'src/app/services/storage/user-storage.service';

@Component({
  selector: 'app-view-product-detail',
  templateUrl: './view-product-detail.component.html',
  styleUrls: ['./view-product-detail.component.css']
})
export class ViewProductDetailComponent {
  productId: number = this.activatedRoute.snapshot.params["productId"];
  product: any;
  FAQS: any[] = [];
  reviews: any[] = [];
  isInWishlist: boolean = false; // ✅ Add this line

  constructor(
    private customerService: CustomerService,
    private snackBar: MatSnackBar,
    private activatedRoute: ActivatedRoute
  ) {}

  averageRating: number = 0;


  ngOnInit() {
  this.getProductDetailById();
}




getProductDetailById() {
  this.customerService.getProductDetailById(this.productId).subscribe(res => {
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


  addToWishlist() {
    const wishlistDto = {
      productId: this.productId,
      userId: UserStorageService.getUserId()
    };

    this.customerService.addProductToWishlist(wishlistDto).subscribe({
      next: (res) => {
        if (res.id != null) {
          this.isInWishlist = true; // ✅ update state when added
          this.snackBar.open('Produit ajouté à la liste de souhaits ❤️', 'Fermer', {
            duration: 4000
          });
        }
      },
      error: (err) => {
        if (err.status === 409) {
          this.isInWishlist = true; // ✅ mark as already in wishlist
          this.snackBar.open('Déjà dans votre liste de souhaits 💖', 'Fermer', {
            duration: 4000
          });
        } else {
          this.snackBar.open('Une erreur est survenue ❌', 'Fermer', {
            duration: 4000
          });
        }
      }
    });
  }
}
