import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators, AbstractControl, ValidationErrors } from '@angular/forms';
import { AdminService } from '../../service/admin.service';
import { Router } from '@angular/router';
import { MatSnackBar } from '@angular/material/snack-bar';

// 🧩 Fonction de validation de date future
function futureDateValidator(control: AbstractControl): ValidationErrors | null {
  if (!control.value) return null;

  const selectedDate = new Date(control.value);
  const today = new Date();

  // On compare uniquement les dates (pas les heures)
  today.setHours(0, 0, 0, 0);
  selectedDate.setHours(0, 0, 0, 0);

  // Vérifie que la date sélectionnée est strictement supérieure à aujourd'hui
  return selectedDate > today ? null : { notFutureDate: true };
}

// 🧩 Fonction de validation pour le pourcentage
function discountValidator(control: AbstractControl): ValidationErrors | null {
  if (!control.value) return null;
  
  const discount = Number(control.value);
  if (isNaN(discount)) {
    return { invalidNumber: true };
  }
  if (discount < 1) {
    return { minDiscount: true };
  }
  if (discount > 100) {
    return { maxDiscount: true };
  }
  return null;
}

// 🧩 Fonction de validation pour le code (lettres et chiffres uniquement)
function codeValidator(control: AbstractControl): ValidationErrors | null {
  if (!control.value) return null;
  
  const codeRegex = /^[A-Z0-9]+$/;
  if (!codeRegex.test(control.value.toUpperCase())) {
    return { invalidCode: true };
  }
  return null;
}

@Component({
  selector: 'app-post-coupon',
  templateUrl: './post-coupon.component.html',
  styleUrls: ['./post-coupon.component.css']
})
export class PostCouponComponent {

  couponForm!: FormGroup;
  minDate: Date;
  maxDate: Date;

  constructor(
    private fb: FormBuilder,
    private snackBar: MatSnackBar,
    private adminService: AdminService,
    private router: Router
  ) {
    // Date minimale : demain
    this.minDate = new Date();
    this.minDate.setDate(this.minDate.getDate() + 1);
    
    // Date maximale : 2 ans à partir d'aujourd'hui
    this.maxDate = new Date();
    this.maxDate.setFullYear(this.maxDate.getFullYear() + 2);
  }

  ngOnInit() {
    this.couponForm = this.fb.group({
      name: [null, [Validators.required, Validators.minLength(3), Validators.maxLength(50)]],
      code: [null, [Validators.required, Validators.minLength(3), Validators.maxLength(20), codeValidator]],
      discount: [null, [Validators.required, discountValidator]],
      expirationDate: [null, [Validators.required, futureDateValidator]],
    });
  }

  // Transformation automatique du code en majuscules
  onCodeInput(event: any) {
    const input = event.target as HTMLInputElement;
    input.value = input.value.toUpperCase();
    this.couponForm.get('code')?.setValue(input.value, { emitEvent: false });
  }

  addCoupon() {
    if (this.couponForm.valid) {
      // Préparer les données avec le code en majuscules
      const couponData = {
        ...this.couponForm.value,
        code: this.couponForm.value.code.toUpperCase()
      };

      this.adminService.addCoupon(couponData).subscribe({
        next: (res) => {
          if (res.id != null) {
            this.snackBar.open('✅ Code promo ajouté avec succès', 'Fermer', {
              duration: 5000,
              panelClass: 'success-snackbar'
            });
            this.router.navigateByUrl('/admin/dashboard');
          } else {
            this.snackBar.open(`❌ ${res.message || 'Erreur lors de la création'}`, 'Fermer', {
              duration: 5000,
              panelClass: 'error-snackbar'
            });
          }
        },
        error: (error) => {
          let errorMessage = 'Erreur lors de l\'ajout du code promo';
          if (error.error && error.error.message) {
            errorMessage = error.error.message;
          } else if (error.status === 409) {
            errorMessage = 'Ce code promo existe déjà';
          }
          this.snackBar.open(`❌ ${errorMessage}`, 'Fermer', {
            duration: 5000,
            panelClass: 'error-snackbar'
          });
        }
      });
    } else {
      this.couponForm.markAllAsTouched();
      this.snackBar.open('❌ Veuillez corriger les erreurs dans le formulaire', 'Fermer', {
        duration: 5000,
        panelClass: 'error-snackbar'
      });
    }
  }

  // Getters pour un accès facile aux contrôles dans le template
  get name() { return this.couponForm.get('name'); }
  get code() { return this.couponForm.get('code'); }
  get discount() { return this.couponForm.get('discount'); }
  get expirationDate() { return this.couponForm.get('expirationDate'); }
}