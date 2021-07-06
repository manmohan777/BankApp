package com.example.bankapp.Database.ViewModel;


public interface CompletableListener<T> extends CompletionListener<T> {

    <T> T run();
}
