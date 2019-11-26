package com.sport.x.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sport.x.AdminActivities.CustomerDetailsActivity;
import com.sport.x.Models.Customer;
import com.sport.x.R;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AllVendorsAdapter extends RecyclerView.Adapter<AllVendorsAdapter.VendorViewHolder> {

    private Context context;
    private ArrayList<Customer> vendorsListModel;

    public AllVendorsAdapter(Context context, ArrayList<Customer> vendorsListModel){
        this.context = context;
        this.vendorsListModel = vendorsListModel;
    }

    @NonNull
    @Override
    public VendorViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.customer_item, viewGroup, false);
        return new VendorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VendorViewHolder vendorViewHolder, int i) {
        vendorViewHolder.setData(vendorsListModel.get(i));
    }

    @Override
    public int getItemCount() {
        return vendorsListModel.size();
    }

    public class VendorViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private CircularImageView customer_img;
        private TextView customer_nam;

        public VendorViewHolder(@NonNull View itemView) {
            super(itemView);

            customer_img = itemView.findViewById(R.id.customer_image);
            customer_nam = itemView.findViewById(R.id.customer_name);

            itemView.setOnClickListener(this);
        }


        public void setData(Customer customer){
            customer_nam.setText(customer.getCustomerName());

            if(customer.getCustomerPicture().isEmpty()){
                customer_img.setImageResource(R.drawable.usersicon);
            }
            else{
                Picasso.get()
                        .load(customer.getCustomerPicture())
                        .into(customer_img);
            }
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context, CustomerDetailsActivity.class);
            intent.putExtra("vendors", 1);
            intent.putExtra("name", vendorsListModel.get(getAdapterPosition()).getCustomerName());
            intent.putExtra("email", vendorsListModel.get(getAdapterPosition()).getCustomerEmail());
            intent.putExtra("phone", vendorsListModel.get(getAdapterPosition()).getCustomerContact());

            intent.putExtra("image", vendorsListModel.get(getAdapterPosition()).getCustomerPicture());
//            intent.putExtra("status", vendorsListModel.get(getAdapterPosition()).getCustomerStatus());
//            intent.putExtra("id", vendorsListModel.get(getAdapterPosition()).getCustomerId());

            context.startActivity(intent);
            ((Activity) context).finish();

        }
    }
}
