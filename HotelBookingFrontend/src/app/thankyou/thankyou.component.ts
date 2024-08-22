import { Component } from '@angular/core';
import { CartService } from '../services/cart.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-thankyou',
  standalone: true,
  imports: [],
  templateUrl: './thankyou.component.html',
  styleUrl: './thankyou.component.css'
})
export class ThankyouComponent {

  constructor(private cartService: CartService, private router:Router){

  }

  ngOnInit(){
    this.cartService.removeAllBookings();
    setTimeout(
      () => this.router.navigateByUrl("/rooms"),5000
    )
  }

  

}
