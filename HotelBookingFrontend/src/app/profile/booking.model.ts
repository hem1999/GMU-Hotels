interface RoomIdsNamesMap {
  [key: string]: string;
}

export interface Booking {
  bookingId: Number;
  bookingDate: String;
  startDate: String;
  endDate: String;
  roomId: Number;
  customerId: Number;
  customerName: String;
  roomAddress: String;
  roomCity: String;
  roomState: String;
  roomZip: String;
  roomCountry: String;
  roomPhone: String;
  roomMainImgSrc: String;
  roomName: String;
}

export type addBookingType = {
  userId: string | null;
  startDate: string;
  endDate: string;
  bookingDate: string;
  roomId: string;
};

export type updateBookingType = {
  bookingId: Number;
  roomId: Number;
  startDate: String;
  endDate: String;
};
