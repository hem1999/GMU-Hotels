import { Component, computed, inject, signal, WritableSignal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { Router, RouterLink,RouterOutlet } from '@angular/router';
import { AuthService } from './services/auth.service';
@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, ReactiveFormsModule, RouterLink],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'hotelFrontend';
  router = inject(Router);
  
  authService = inject(AuthService);
  IsLoggedIn = false;
  showProfileMenu=false;
  toggleShowMyProfileMenu(){
    this.showProfileMenu = !this.showProfileMenu;
  }

  ngOnInit(){
    this.IsLoggedIn = this.authService.checkLoginStatus();
    this.authService.isLoggedIn$.subscribe(
      res => this.IsLoggedIn = res
    )
    
  }

  setUpUserId(){
    this.router.navigateByUrl(`profile/${this.authService.getUserId()}`);
  }

  
}
