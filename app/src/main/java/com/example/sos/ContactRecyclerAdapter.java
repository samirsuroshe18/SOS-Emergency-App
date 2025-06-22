//package com.example.sos;
//
//import android.Manifest;
//import android.app.Activity;
//import android.app.AlertDialog;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.net.Uri;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.core.app.ActivityCompat;
//import androidx.core.content.ContextCompat;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.google.android.material.button.MaterialButton;
//
//import java.util.ArrayList;
//
//public class ContactRecyclerAdapter extends RecyclerView.Adapter<ContactRecyclerAdapter.ViewHolder> {
//    private static final int REQUEST_CALL_PHONE_PERMISSION = 1;
//    Context context;
//    ArrayList<ContactModel> modelArrayList;
//    DatabaseHelper databaseHelper;
//
//    public ContactRecyclerAdapter(Context context, ArrayList<ContactModel> modelArrayList) {
//        this.context = context;
//        this.modelArrayList = modelArrayList;
//    }
//
//    @NonNull
//    @Override
//    public ContactRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(context).inflate(R.layout.contact_recycler_view, parent, false);
//        return new ViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull ContactRecyclerAdapter.ViewHolder holder, int position) {
//        ContactModel currentModel = modelArrayList.get(position);
//
//        holder.name.setText(currentModel.getName());
//        holder.number.setText(currentModel.getNumber());
//
//        // Call button click listener
//        holder.callButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                int adapterPosition = holder.getAdapterPosition();
//                if (adapterPosition != RecyclerView.NO_POSITION) {
//                    ContactModel model = modelArrayList.get(adapterPosition);
//
//                    if (ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
//                        // Permission is not granted, request it
//                        ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL_PHONE_PERMISSION);
//                    } else {
//                        // Permission is already granted, make the phone call
//                        Intent intent = new Intent(Intent.ACTION_CALL);
//                        intent.setData(Uri.parse("tel:" + model.getNumber()));
//                        v.getContext().startActivity(intent);
//                    }
//                }
//            }
//        });
//
//        // Delete button click listener
//        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                int adapterPosition = holder.getAdapterPosition();
//                if (adapterPosition != RecyclerView.NO_POSITION) {
//                    ContactModel model = modelArrayList.get(adapterPosition);
//
//                    new AlertDialog.Builder(context)
//                            .setTitle("Delete")
//                            .setMessage("Are you sure you want to delete this contact?")
//                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    databaseHelper = new DatabaseHelper(context);
//                                    boolean checkData = databaseHelper.deleteData(model.getId());
//                                    if (checkData) {
//                                        Toast.makeText(context, "Contact Deleted", Toast.LENGTH_SHORT).show();
//                                        modelArrayList.remove(adapterPosition);
//                                        notifyItemRemoved(adapterPosition);
//                                        notifyItemRangeChanged(adapterPosition, modelArrayList.size());
//                                    } else {
//                                        Toast.makeText(context, "Failed to delete contact", Toast.LENGTH_SHORT).show();
//                                    }
//                                }
//                            })
//                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    dialog.dismiss();
//                                }
//                            })
//                            .show();
//                }
//            }
//        });
//
//        // Long click listener for item view
//        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View view) {
//                int adapterPosition = holder.getAdapterPosition();
//                if (adapterPosition != RecyclerView.NO_POSITION) {
//                    ContactModel model = modelArrayList.get(adapterPosition);
//
//                    new AlertDialog.Builder(context)
//                            .setTitle("Delete")
//                            .setMessage("Are you sure you want to delete this contact?")
//                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    databaseHelper = new DatabaseHelper(context);
//                                    boolean checkData = databaseHelper.deleteData(model.getId());
//                                    if (checkData) {
//                                        Toast.makeText(context, "Contact Deleted", Toast.LENGTH_SHORT).show();
//                                        modelArrayList.remove(adapterPosition);
//                                        notifyItemRemoved(adapterPosition);
//                                        notifyItemRangeChanged(adapterPosition, modelArrayList.size());
//                                    } else {
//                                        Toast.makeText(context, "Failed to delete contact", Toast.LENGTH_SHORT).show();
//                                    }
//                                }
//                            })
//                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    dialog.dismiss();
//                                }
//                            })
//                            .show();
//                }
//                return true;
//            }
//        });
//    }
//
//    @Override
//    public int getItemCount() {
//        return modelArrayList.size();
//    }
//
//    public static class ViewHolder extends RecyclerView.ViewHolder {
//        TextView name, number;
//        MaterialButton callButton, deleteButton;
//
//        public ViewHolder(@NonNull View itemView) {
//            super(itemView);
//            name = itemView.findViewById(R.id.recyclerContactName);
//            number = itemView.findViewById(R.id.recyclerContactNumber);
//            callButton = itemView.findViewById(R.id.callButton);
//            deleteButton = itemView.findViewById(R.id.deleteButton);
//        }
//    }
//}




package com.example.sos;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

public class ContactRecyclerAdapter extends RecyclerView.Adapter<ContactRecyclerAdapter.ViewHolder> {
    private static final int REQUEST_CALL_PHONE_PERMISSION = 1;
    Context context;
    ArrayList<ContactModel> modelArrayList;
    DatabaseHelper databaseHelper;
    OnContactDeleteListener deleteListener;

    // Interface for callback when contact is deleted
    public interface OnContactDeleteListener {
        void onContactDeleted();
    }

    public ContactRecyclerAdapter(Context context, ArrayList<ContactModel> modelArrayList, OnContactDeleteListener deleteListener) {
        this.context = context;
        this.modelArrayList = modelArrayList;
        this.deleteListener = deleteListener;
    }

    @NonNull
    @Override
    public ContactRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.contact_recycler_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactRecyclerAdapter.ViewHolder holder, int position) {
        ContactModel currentModel = modelArrayList.get(position);

        holder.name.setText(currentModel.getName());
        holder.number.setText(currentModel.getNumber());

        // Call button click listener
        holder.callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int adapterPosition = holder.getAdapterPosition();
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    ContactModel model = modelArrayList.get(adapterPosition);

                    if (ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        // Permission is not granted, request it
                        ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL_PHONE_PERMISSION);
                    } else {
                        // Permission is already granted, make the phone call
                        Intent intent = new Intent(Intent.ACTION_CALL);
                        intent.setData(Uri.parse("tel:" + model.getNumber()));
                        v.getContext().startActivity(intent);
                    }
                }
            }
        });

        // Delete button click listener
        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int adapterPosition = holder.getAdapterPosition();
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    ContactModel model = modelArrayList.get(adapterPosition);

                    new AlertDialog.Builder(context)
                            .setTitle("Delete")
                            .setMessage("Are you sure you want to delete this contact?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    databaseHelper = new DatabaseHelper(context);
                                    boolean checkData = databaseHelper.deleteData(model.getId());
                                    if (checkData) {
                                        Toast.makeText(context, "Contact Deleted", Toast.LENGTH_SHORT).show();
                                        modelArrayList.remove(adapterPosition);
                                        notifyItemRemoved(adapterPosition);
                                        notifyItemRangeChanged(adapterPosition, modelArrayList.size());

                                        // Notify the activity that a contact was deleted
                                        if (deleteListener != null) {
                                            deleteListener.onContactDeleted();
                                        }
                                    } else {
                                        Toast.makeText(context, "Failed to delete contact", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .show();
                }
            }
        });

        // Long click listener for item view
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                int adapterPosition = holder.getAdapterPosition();
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    ContactModel model = modelArrayList.get(adapterPosition);

                    new AlertDialog.Builder(context)
                            .setTitle("Delete")
                            .setMessage("Are you sure you want to delete this contact?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    databaseHelper = new DatabaseHelper(context);
                                    boolean checkData = databaseHelper.deleteData(model.getId());
                                    if (checkData) {
                                        Toast.makeText(context, "Contact Deleted", Toast.LENGTH_SHORT).show();
                                        modelArrayList.remove(adapterPosition);
                                        notifyItemRemoved(adapterPosition);
                                        notifyItemRangeChanged(adapterPosition, modelArrayList.size());

                                        // Notify the activity that a contact was deleted
                                        if (deleteListener != null) {
                                            deleteListener.onContactDeleted();
                                        }
                                    } else {
                                        Toast.makeText(context, "Failed to delete contact", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .show();
                }
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return modelArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, number;
        MaterialButton callButton, deleteButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.recyclerContactName);
            number = itemView.findViewById(R.id.recyclerContactNumber);
            callButton = itemView.findViewById(R.id.callButton);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }
}