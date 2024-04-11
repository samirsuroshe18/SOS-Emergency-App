package com.example.sos;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ContactRecyclerAdapter extends RecyclerView.Adapter<ContactRecyclerAdapter.ViewHolder> {

    Context context;
    ArrayList<ContactModel> modelArrayList;
    DatabaseHelper databaseHelper;
    ContactModel model;

    public ContactRecyclerAdapter(Context context, ArrayList<ContactModel> modelArrayList) {
        this.context = context;
        this.modelArrayList = modelArrayList;
    }

    @NonNull
    @Override
    public ContactRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.contact_recycler_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactRecyclerAdapter.ViewHolder holder, int position) {
        model = modelArrayList.get(position);

        String id = model.getId();;
            ((ViewHolder) holder).name.setText(model.getName());
            ((ViewHolder) holder).number.setText(model.getNumber());

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {

                    new AlertDialog.Builder(context).setTitle("Delete").setMessage("Are you sure you want to delete this message")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    databaseHelper = new DatabaseHelper(context);
                                    boolean checkData = databaseHelper.deleteData(id);
                                    if (checkData){
                                        Toast.makeText(context, "Data is Deleted", Toast.LENGTH_SHORT).show();
                                        modelArrayList.remove(model);
                                        notifyItemRemoved(modelArrayList.indexOf(model));
                                        notifyDataSetChanged();
                                    }
                                    else {
                                        Toast.makeText(context, "Data is not Deleted", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).show();

                    return true;
                }
            });
    }

    @Override
    public int getItemCount() {
        return modelArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView name, number;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.recyclerContactName);
            number = itemView.findViewById(R.id.recyclerContactNumber);
        }
    }
}
