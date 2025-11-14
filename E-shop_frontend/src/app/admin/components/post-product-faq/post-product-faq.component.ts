import { Component } from '@angular/core';
import { AdminService } from '../../service/admin.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router , ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-post-product-faq',
  templateUrl: './post-product-faq.component.html',
  styleUrls: ['./post-product-faq.component.css']
})
export class PostProductFaqComponent {


  productId: number = this.activatedRoute.snapshot.params["productId"];

  FAQForm!: FormGroup;


    constructor(
      private fb: FormBuilder,
      private router: Router,
      private snackBar: MatSnackBar,
      private adminService: AdminService,
      private activatedRoute: ActivatedRoute
    ){}

    ngOnInit(){
      this.FAQForm = this.fb.group({
        question: [null,[Validators.required]],
        answer: [null,[Validators.required]],
      })
    }

postFAQ() {
  if (this.FAQForm.invalid) {
    this.snackBar.open('Please fill in all required fields', 'close', {
      duration: 3000,
      panelClass: 'error-snackbar'
    });
    return;
  }

  this.adminService.postFAQ(this.productId, this.FAQForm.value).subscribe({
    next: (res) => {
      if (res && res.id != null) {
        this.snackBar.open('FAQ Posted Successfully', 'close', { duration: 3000 });
        this.router.navigateByUrl('/admin/dashboard');
      } else {
        this.snackBar.open('Something went wrong', 'close', {
          duration: 3000,
          panelClass: 'error-snackbar'
        });
      }
    },
    error: (err) => {
      this.snackBar.open('Error while posting FAQ', 'close', {
        duration: 3000,
        panelClass: 'error-snackbar'
      });
    }
  });
}


}
