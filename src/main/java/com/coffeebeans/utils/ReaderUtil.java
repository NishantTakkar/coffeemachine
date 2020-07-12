package com.coffeebeans.utils;

import com.coffeebeans.request.OrderRequestDTO;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class ReaderUtil {


    public static String getJsonString(File file) throws FileNotFoundException {

        Scanner sc = new Scanner(file);

        StringBuilder stringBuilder=new StringBuilder();
        while (sc.hasNextLine()){
            stringBuilder.append(sc.nextLine());
        }

        return stringBuilder.toString();

    }

    public static OrderRequestDTO getOrderRequest(String jsonData){

        Gson gson=new Gson();
        return gson.fromJson(jsonData, OrderRequestDTO.class);

    }
}
