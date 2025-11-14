import { Component, OnInit } from '@angular/core';
import { CustomerService } from '../../services/customer.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatDialog } from '@angular/material/dialog';
import { Router } from '@angular/router';

@Component({
  selector: 'app-place-order',
  templateUrl: './place-order.component.html',
  styleUrls: ['./place-order.component.css']
})
export class PlaceOrderComponent implements OnInit {
  orderForm!: FormGroup;

  constructor(
    private customerService: CustomerService,
    private fb: FormBuilder,
    private snackBar: MatSnackBar,
    private router: Router,
    private dialog: MatDialog
  ) {}

  ngOnInit(){
    this.orderForm = this.fb.group({
      address: [null, [Validators.required]],
      orderDescription: [null],
      
    });
  }

  placeOrder() {
   

    this.customerService.placeOrder(this.orderForm.value).subscribe(res => {
      if (res.id != null) {
        this.snackBar.open('Commande passée avec succès', 'close', { duration: 5000 })
        this.router.navigateByUrl('/customer/my_orders');
        this.closeForm();
      } else {
        this.snackBar.open('Oups… une erreur s’est produite', 'close', { duration: 5000 })
      }
    })
  }

  closeForm() {
    this.dialog.closeAll();
  }
}
