package com.example.task;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.task.adapter.DocumentAdapter;
import com.example.task.db.DatabaseHelper;
import com.example.task.model.Document;
import java.util.ArrayList;
import java.util.List;

public class DocumentsActivity extends AppCompatActivity {
    private RecyclerView documentRecyclerView;
    private DocumentAdapter documentAdapter;
    private List<Document> documentList;
    private DatabaseHelper db;
    private static final int PICK_FILE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_documents);

        documentRecyclerView = findViewById(R.id.documentRecyclerView);
        Button addDocumentButton = findViewById(R.id.addDocumentButton);
        db = new DatabaseHelper(this);
        documentList = new ArrayList<>();

        documentAdapter = new DocumentAdapter(documentList);
        documentRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        documentRecyclerView.setAdapter(documentAdapter);

        loadDocuments();

        addDocumentButton.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("*/*");
            startActivityForResult(intent, PICK_FILE_REQUEST);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_FILE_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            String path = uri.toString();
            String name = path.substring(path.lastIndexOf("/") + 1);

            Document document = new Document();
            document.setName(name);
            document.setPath(path);
            db.addDocument(document);

            loadDocuments();
        }
    }

    private void loadDocuments() {
        documentList.clear();
        documentList.addAll(db.getAllDocuments());
        documentAdapter.notifyDataSetChanged();
    }
}