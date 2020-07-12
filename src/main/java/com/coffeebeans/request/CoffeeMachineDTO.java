package com.coffeebeans.request;

import com.coffeebeans.enums.BeverageType;
import com.coffeebeans.enums.IngredientType;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class CoffeeMachineDTO {

     OutletDTO outlets;
     Map<IngredientType, Integer> total_items_quantity;
     Map<BeverageType, Map<IngredientType, Integer>> beverages = new HashMap<>();

}
