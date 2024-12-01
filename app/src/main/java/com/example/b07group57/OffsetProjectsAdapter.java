package com.example.b07group57;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.b07group57.models.OffsetProject;

import java.util.List;

public class OffsetProjectsAdapter extends RecyclerView.Adapter<OffsetProjectsAdapter.ViewHolder> {

    private List<OffsetProject> offsetProjects;
    private Context context;

    public OffsetProjectsAdapter(List<OffsetProject> offsetProjects) {
        this.offsetProjects = offsetProjects;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_offset_project, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OffsetProject project = offsetProjects.get(position);
        holder.tvProjectName.setText(project.getName());
        holder.tvProjectLocation.setText("Location: " + project.getLocation());
        holder.tvProjectDescription.setText(project.getDescription());
        holder.tvProjectCost.setText(String.format("Cost: $%.2f per ton CO2e", project.getCostPerTon()));

        holder.tvProjectLink.setText("Learn more, or donate");
        holder.tvProjectLink.setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(project.getWebsiteUrl()));
            context.startActivity(browserIntent);
        });

        holder.btnPurchase.setOnClickListener(v -> {
            String inputTons = holder.etCo2eTons.getText().toString().trim();
            if (inputTons.isEmpty()) {
                Toast.makeText(context, "Please enter the amount of CO2e to offset.", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                double tons = Double.parseDouble(inputTons);
                double totalCost = tons * project.getCostPerTon();
                String message = String.format("Offset %.2f tons of CO2e for $%.2f by supporting this offset project.",
                        tons, totalCost);
                Toast.makeText(context, message, Toast.LENGTH_LONG).show();
            } catch (NumberFormatException e) {
                Toast.makeText(context, "Invalid input. Please enter a valid number.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return offsetProjects.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvProjectName, tvProjectLocation, tvProjectDescription, tvProjectCost, tvProjectLink;
        EditText etCo2eTons;
        Button btnPurchase;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvProjectName = itemView.findViewById(R.id.tv_project_name);
            tvProjectLocation = itemView.findViewById(R.id.tv_project_location);
            tvProjectDescription = itemView.findViewById(R.id.tv_project_description);
            tvProjectCost = itemView.findViewById(R.id.tv_project_cost);
            tvProjectLink = itemView.findViewById(R.id.tv_project_link);
            etCo2eTons = itemView.findViewById(R.id.et_co2e_tons);
            btnPurchase = itemView.findViewById(R.id.btn_purchase);
        }
    }
}
