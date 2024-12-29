package com.example.demo.data.pipeline;


import java.io.IOException;
import java.text.ParseException;

public interface Filter<T> {
    T execute(T input) throws IOException, ParseException;
}