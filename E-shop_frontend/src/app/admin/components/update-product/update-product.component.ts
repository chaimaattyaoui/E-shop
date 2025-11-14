import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ActivatedRoute, Router } from '@angular/router';
import { AdminService } from '../../service/admin.service';

@Component({
  selector: 'app-update-product',
  templateUrl: './update-product.component.html',
  styleUrls: ['./update-product.component.css']
})
export class UpdateProductComponent implements OnInit {

  productId!: number;
  productForm!: FormGroup;
  listofCategories: any[] = [];
  selectedFile: File | null = null;
  imagePreview: string | ArrayBuffer | null = null;
  existingImage: string | null = null;
  imgChanged = false;

  constructor(
    private fb: FormBuilder,
    private router: Router,
    private snackBar: MatSnackBar,
    private adminService: AdminService,
    private activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    // read productId from route params
    const param = this.activatedRoute.snapshot.paramMap.get('productId');
    if (!param) {
      this.snackBar.open('No productId in route', 'Close', { duration: 3000 });
      this.router.navigateByUrl('/admin/dashboard');
      return;
    }
    this.productId = +param;

    // init form
    this.productForm = this.fb.group({
      categoryId: [null, [Validators.required]],
      name: [null, [Validators.required]],
      price: [null, [Validators.required]],
      description: [null, [Validators.required]],
    });

    // load options + product
    this.getAllCategories();
    this.getProductById();
  }

  onFileSelected(event: any): void {
    const file: File | undefined = event?.target?.files?.[0];
    if (!file) {
      return;
    }

    this.selectedFile = file;
    this.imgChanged = true;
    this.existingImage = null; // hide existing Cloudinary image when user selects a new one
    this.previewImage();
  }

  previewImage(): void {
    if (!this.selectedFile) {
      this.imagePreview = null;
      return;
    }
    const reader = new FileReader();
    reader.onload = () => {
      this.imagePreview = reader.result;
    };
    reader.readAsDataURL(this.selectedFile);
  }

  getAllCategories(): void {
    this.adminService.getAllCategories().subscribe({
      next: (res) => this.listofCategories = res ?? [],
      error: (err) => {
        console.error('Failed to load categories', err);
        this.snackBar.open('Failed to load categories', 'Close', { duration: 3000 });
      }
    });
  }

  getProductById(): void {
    this.adminService.getProductById(this.productId).subscribe({
      next: (res) => {
        if (!res) {
          this.snackBar.open('Product not found', 'Close', { duration: 3000 });
          this.router.navigateByUrl('/admin/dashboard');
          return;
        }

        // Patch the form with product fields
        this.productForm.patchValue({
          categoryId: res.categoryId ?? null,
          name: res.name ?? null,
          price: res.price ?? null,
          description: res.description ?? null
        });

        // Use Cloudinary imageUrl if provided
        if (res.imageUrl) {
          this.existingImage = res.imageUrl;
        } else {
          this.existingImage = null;
        }
      },
      error: (err) => {
        console.error('Failed to load product', err);
        this.snackBar.open('Failed to load product', 'Close', { duration: 3000 });
        this.router.navigateByUrl('/admin/dashboard');
      }
    });
  }

  updateProduct(): void {
    if (!this.productForm.valid) {
      // mark controls so errors display
      Object.keys(this.productForm.controls).forEach(key => {
        const control = this.productForm.controls[key];
        control.markAsDirty();
        control.updateValueAndValidity();
      });
      return;
    }

    const formData: FormData = new FormData();

    // Only append the image if the user changed it
    if (this.imgChanged && this.selectedFile) {
      formData.append('img', this.selectedFile, this.selectedFile.name);
    }

    // Append other fields (ensure strings)
    formData.append('categoryId', String(this.productForm.get('categoryId')!.value));
    formData.append('name', String(this.productForm.get('name')!.value));
    formData.append('description', String(this.productForm.get('description')!.value));
    formData.append('price', String(this.productForm.get('price')!.value));

    this.adminService.updateProduct(this.productId, formData).subscribe({
      next: (res) => {
        // assume success returns the updated product DTO with an id
        if (res && res.id != null) {
          this.snackBar.open('Product updated successfully', 'Close', { duration: 4000 });
          this.router.navigateByUrl('/admin/dashboard');
        } else if (res && res.message) {
          this.snackBar.open(res.message, 'Error', { duration: 5000 });
        } else {
          this.snackBar.open('Unexpected response from server', 'Error', { duration: 4000 });
        }
      },
      error: (err) => {
        console.error('Update failed', err);
        const msg = err?.error?.message ?? 'Failed to update product';
        this.snackBar.open(msg, 'Error', { duration: 5000 });
      }
    });
  }

  // convenience getter for template if needed
  get f() {
    return this.productForm.controls;
  }
}
