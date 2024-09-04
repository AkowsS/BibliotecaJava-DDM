package com.example.bibliotecajava;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bibliotecajava.model.Book;

import java.util.List;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> {

    private List<Book> bookList;
    private LayoutInflater inflater;
    private Context context;
    private String userEmail;

    public BookAdapter(Context context, List<Book> bookList, String userEmail) {
        this.bookList = bookList;
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        this.userEmail = userEmail;
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.item_book, parent, false);
        return new BookViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        Book book = bookList.get(position);
        holder.textViewTitle.setText(book.getTitle());
        holder.textViewPublisher.setText(book.getPublisher());
        holder.textViewGenre.setText(book.getGenre());

        if (book.getCoverImage() != null) {
            holder.imageViewCover.setImageBitmap(BitmapFactory.decodeByteArray(book.getCoverImage(), 0, book.getCoverImage().length));
        } else {
            holder.imageViewCover.setImageResource(R.drawable.baseline_book_24);
        }

        holder.itemView.setOnClickListener(v -> {
            int bookId = book.getId();

            Intent intent = new Intent(context, EditBookActivity.class);
            intent.putExtra("BOOK_ID", bookId);
            intent.putExtra("USER_EMAIL", userEmail);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }

    static class BookViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewCover;
        TextView textViewTitle;
        TextView textViewPublisher;
        TextView textViewGenre;

        public BookViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewCover = itemView.findViewById(R.id.imageViewCover);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            textViewPublisher = itemView.findViewById(R.id.textViewPublisher);
            textViewGenre = itemView.findViewById(R.id.textViewGenre);
        }
    }
}
