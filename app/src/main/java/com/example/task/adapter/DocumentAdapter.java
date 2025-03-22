package com.example.task.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.task.R;
import com.example.task.model.Document;
import java.util.List;

public class DocumentAdapter extends RecyclerView.Adapter<DocumentAdapter.DocumentViewHolder> {
    private List<Document> documentList;

    public DocumentAdapter(List<Document> documentList) {
        this.documentList = documentList;
    }

    @NonNull
    @Override
    public DocumentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_document, parent, false);
        return new DocumentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DocumentViewHolder holder, int position) {
        Document document = documentList.get(position);
        holder.nameTextView.setText(document.getName());
        holder.pathTextView.setText(document.getPath());
    }

    @Override
    public int getItemCount() {
        return documentList.size();
    }

    public static class DocumentViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView, pathTextView;

        public DocumentViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            pathTextView = itemView.findViewById(R.id.pathTextView);
        }
    }
}