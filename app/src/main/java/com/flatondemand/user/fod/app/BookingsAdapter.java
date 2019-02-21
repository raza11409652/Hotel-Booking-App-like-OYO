package com.flatondemand.user.fod.app;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.flatondemand.user.fod.R;
import com.flatondemand.user.fod.model.BookingList;

import java.util.List;

public class BookingsAdapter extends     RecyclerView.Adapter<BookingsAdapter.MyViewHolder> {
    List<BookingList> lists;
    private Context context;
    SQLiteHandler sqlLiteHandler;
    public  BookingsAdapter(Context context , List<BookingList>list){
        this.context=context;
        this.lists=list;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View layout= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.single_booking_view,viewGroup ,false);

        return new BookingsAdapter.MyViewHolder(layout);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
    /*
    *    holderView.bookedOn.setText("Booked On : "+list.get(i).getBookedOn());
            holderView.bookingNo.setText("Booking No : "+list.get(i).getBookingNo());
            holderView.bookedBy.setText("Booked By : "+list.get(i).getBookedBy());
    * */
    myViewHolder.bookingno.setText("# "+lists.get(i).getBookingNo());
    myViewHolder.bookedOn.setText("Booked On : "+lists.get(i).getBookedOn());
    myViewHolder.dateTo.setText("To : "+lists.get(i).getDateTo());
    myViewHolder.dateFrom.setText("From : "+lists.get(i).getDateFrom());
    myViewHolder.propertyName.setText("FOD : "+lists.get(i).getPropertyName());
    }

    @Override
    public int getItemCount() {
        return lists.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView bookingno , propertyName , bookedOn , dateFrom , dateTo ;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            propertyName=(TextView)itemView.findViewById(R.id.bookedProperty);
            bookedOn=(TextView)itemView.findViewById(R.id.bookingDate);
            dateFrom=(TextView)itemView.findViewById(R.id.fromDate);
            dateTo=(TextView)itemView.findViewById(R.id.toDate);
            bookingno=(TextView)itemView.findViewById(R.id.bookingNo);

        }
    }
}
