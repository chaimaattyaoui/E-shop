import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ActivatedRoute, Router } from '@angular/router';
import { CustomerService } from '../../services/customer.service';
import { UserStorageService } from 'src/app/services/storage/user-storage.service';

@Component({
  selector: 'app-review-ordered-product',
  templateUrl: './review-ordered-product.component.html',
  styleUrls: ['./review-ordered-product.component.css']
})
export class ReviewOrderedProductComponent {

  productId: number = this.activatedroute.snapshot.params["productId"];
  reviewForm!: FormGroup;
  selectedFile: File | null = null;
  imagePreview: string | ArrayBuffer | null = null;

  stars = [1, 2, 3, 4, 5]; // 5-star rating system

  constructor(
    private customerService: CustomerService,
    private fb: FormBuilder,
    private snackBar: MatSnackBar,
    private router: Router,
    private activatedroute: ActivatedRoute
  ) {}

ngOnInit() {
  const orderId = this.activatedroute.snapshot.paramMap.get('orderId');
  if (orderId) {
    this.productId = Number(orderId);   // ou renommer en orderId si plus logique
  }

  this.reviewForm = this.fb.group({
    rating: [null, [Validators.required]],
    description: [null, [Validators.required]],
  });

  this.customerService.getOrderedProducts(this.productId).subscribe(res => {
    const existingReview = res.reviews.find((r: any) => r.userId === UserStorageService.getUserId());
    if (existingReview) {
      this.reviewForm.patchValue({
        rating: existingReview.rating,
        description: existingReview.description
      });
      this.imagePreview = existingReview.processedImg || null;
    }
  });
}


  setRating(value: number) {
    this.reviewForm.get('rating')?.setValue(value);
  }

  onFileSelected(event: any) {
    this.selectedFile = event.target.files[0];
    this.previewImage();
  }

  previewImage() {
    if (!this.selectedFile) return;
    const reader = new FileReader();
    reader.onload = () => {
      this.imagePreview = reader.result;
    };
    reader.readAsDataURL(this.selectedFile);
  }

  submitForm() {
    if (this.reviewForm.invalid) {
      this.snackBar.open('Please fill all required fields', 'ERROR', {
        duration: 5000,
      });
      return;
    }

    const user = UserStorageService.getUser();
    if (!user || (!user.id && !user.userId)) {
      this.snackBar.open('User not logged in or user ID not found', 'ERROR', {
        duration: 5000,
      });
      return;
    }

    const userId = user.id ? user.id.toString() : user.userId ? user.userId.toString() : '';
    if (!userId) {
      this.snackBar.open('Invalid user ID', 'ERROR', { duration: 5000 });
      return;
    }

    const formData: FormData = new FormData();
    if (this.selectedFile) {
      formData.append('img', this.selectedFile);
    }
if (!this.productId || isNaN(this.productId)) {
  this.snackBar.open('Invalid order ID', 'ERROR', { duration: 5000 });
  return;
}
formData.append('orderId', this.productId.toString());  // ⚡ attention clé backend

formData.append('productId', this.productId.toString());

    formData.append('userId', userId);

    const ratingValue = this.reviewForm.get('rating')?.value;
    const descriptionValue = this.reviewForm.get('description')?.value;

    if (ratingValue !== null && ratingValue !== undefined) {
      formData.append('rating', ratingValue.toString());
    }

    if (descriptionValue) {
      formData.append('description', descriptionValue);
    }

    this.customerService.giveReview(formData).subscribe(
      (res) => {
        if (res.id != null) {
          this.snackBar.open('Review Posted Successfully', 'Close', {
            duration: 5000,
          });
          this.router.navigateByUrl('/customer/my_orders');
        } else {
          this.snackBar.open('Something went wrong', 'ERROR', { duration: 5000 });
        }
      },
      (error) => {
        this.snackBar.open('Failed to submit review: ' + error.message, 'ERROR', {
          duration: 5000,
        });
      }
    );
  }
}

