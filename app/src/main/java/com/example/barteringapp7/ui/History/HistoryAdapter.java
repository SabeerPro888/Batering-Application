package com.example.barteringapp7.ui.History;

import static androidx.core.content.ContextCompat.startActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.barteringapp7.APIService;
import com.example.barteringapp7.Activity_Offer_Details;
import com.example.barteringapp7.GlobalVariables;
import com.example.barteringapp7.Items;
import com.example.barteringapp7.NavigationActivity;
import com.example.barteringapp7.R;
import com.example.barteringapp7.RetrofitClient;
import com.example.barteringapp7.ViewRequestsInformation;
import com.example.barteringapp7.ui.Notifications.NotificationsViewHolder;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryViewholder> {

    private ArrayList<ViewRequestsInformation> ItemsList;
    private Context context;
    private Context activityContext;

    int MyOwnId;


    public HistoryAdapter(Context context,Context activityContext, ArrayList<ViewRequestsInformation> ItemsList) {
        this.context = context;
        this.ItemsList = ItemsList;
        this.activityContext = activityContext;

    }
    @NonNull
    @Override
    public HistoryViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_history, parent, false);

        return new HistoryViewholder(view);    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewholder holder, int position) {
        ViewRequestsInformation currentItem = ItemsList.get(position);
        holder.SenderName.setText(currentItem.getSender_name());
        holder.ReceiverName.setText(currentItem.getReceiver_name());
        holder.RequestedItem.setText(currentItem.getRequestedItemName());
        // Convert integer price to String


        if("Yes".equals(currentItem.getConfirmOfferRequestReceiver())){
            holder.btnRequestReceiverConfirmation.setEnabled(false);
            holder.btnRequestReceiverConfirmation.setText("Completed");
        }

        if("Yes".equals(currentItem.getConfirmOfferRequestSeder())){
            holder.btnRequestSenderComfirmation.setText("Completed");
            holder.btnRequestSenderComfirmation.setEnabled(false);
        }

        holder.btnRequestReceiverConfirmation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               APIService apiService = RetrofitClient.getRetrofitInstance().create(APIService.class);
                Call<Void> call = apiService.confirmOfferReceiver(currentItem.getOffer_id());
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            Log.e("Receiver Confirmation", "Confirmed");
                            holder.btnRequestReceiverConfirmation.setText("Confirmed");
                            holder.btnRequestReceiverConfirmation.setEnabled(false);

                                showRatingDialog(currentItem.getSenderId(), currentItem.getReceiverId(),currentItem.getSender_name());


                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {

                    }
                });

            }
        });
        holder.btnRequestSenderComfirmation.setVisibility(View.GONE);
        holder.btnRequestReceiverConfirmation.setVisibility(View.GONE);
        holder.btnRequestSenderComfirmation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                APIService apiService = RetrofitClient.getRetrofitInstance().create(APIService.class);
                Call<Void> call = apiService.confirmOfferSender(currentItem.getOffer_id());
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if(response.isSuccessful()){
                            Log.e("Sender Confirmation", "Confirmed");
                            holder.btnRequestSenderComfirmation.setText("Confirmed");
                            holder.btnRequestSenderComfirmation.setEnabled(false);

                            showRatingDialog(currentItem.getReceiverId(), currentItem.getSenderId(),currentItem.getReceiver_name());

                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {

                    }
                });
            }
        });


        MyId(new MyIdCallback() {
            @Override
            public void onSuccess(int myId) {

                if(myId==currentItem.getSenderId()){
                    holder.btnRequestReceiverConfirmation.setEnabled(false);
                    MyOwnId=myId;

                }else if(myId==currentItem.getReceiverId()){
                    holder.btnRequestSenderComfirmation.setEnabled(false);
                    MyOwnId=myId;


                }

            }


            @Override
            public void onFailure(Throwable t) {
                // Handle the failure case here
                // For example, show an error message
            }
        });

    }



    private void showRatingDialog(int senderId, int ReceiverId, String senderName) {
        Dialog dialog = new Dialog(activityContext);  // Use activityContext here
        dialog.setContentView(R.layout.dialog_rating);

        RatingBar ratingBar = dialog.findViewById(R.id.dialog_rating_bar);
        Button submitButton = dialog.findViewById(R.id.submit_rating_button);
        TextView txtSenderName = dialog.findViewById(R.id.dialog_title);
        txtSenderName.setText("Rate " + senderName);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float ratingValue = ratingBar.getRating();
                submitRating(senderId, ReceiverId, ratingValue);
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void submitRating(int senderid, int receiverId, float ratingValue) {
        APIService apiService = RetrofitClient.getRetrofitInstance().create(APIService.class);
        Call<Void> call = apiService.GiveRating(receiverId,senderid, ratingValue);
        Log.e("Receiver Id",String.valueOf(receiverId));
        Log.e("Sender Id",String.valueOf(senderid));
        Log.e("Rating Value",String.valueOf(ratingValue));

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()){
                    Toast.makeText(context.getApplicationContext(), "Thank you for rating",Toast.LENGTH_SHORT);


                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("API Failure","Api went to On Failure");
            }
        });



    }



    public void MyId(final MyIdCallback callback) {
        String email = GlobalVariables.getInstance().getEmail();
        APIService apiService = RetrofitClient.getRetrofitInstance().create(APIService.class);
        Call<String> call = apiService.getUserId(email);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    int myId = Integer.parseInt(response.body());
                    callback.onSuccess(myId);
                } else {
                    callback.onFailure(new Exception("Unsuccessful response"));
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                callback.onFailure(t);
            }
        });
    }



    @Override
    public int getItemCount() {
        return ItemsList.size();
    }
}


