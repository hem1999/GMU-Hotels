import { Component, OnInit, signal, WritableSignal, Input } from '@angular/core';
import { Booking } from './booking.model';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-list-bookings',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './list-bookings.component.html',
  styleUrl: './list-bookings.component.css'
})
export class ListBookingsComponent implements OnInit {
  
  @Input() bookings:{"previous":Booking[], "current":Booking[], "upcoming":Booking[]}={"previous": [], "current":[], "upcoming":[]};
  visibleBookings: Booking[] = [];
  activeTab: Number = 1;
  
  ngOnInit(){
    this.getBookings();
    this.updateBookingsView(this.activeTab);
  }

  getBookings(){
   
  }

  updateBookingsView(currentView:Number){
    this.getBookings();
    this.activeTab = currentView;
    switch (currentView){
      case 2:
        this.visibleBookings = this.bookings.current;
        break;
      case 3:
        this.visibleBookings = this.bookings.previous;
        break;
      default:
        this.visibleBookings = this.bookings.upcoming;
    }

  }
}
