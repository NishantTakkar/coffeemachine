package com.coffeebeans.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class Inventory {

    List<Ingredient> ingredientList=new ArrayList<>();

}
