package com.coffeebeans.entity;

import com.coffeebeans.enums.BeverageType;
import com.coffeebeans.enums.IngredientType;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

@Data
@AllArgsConstructor
public class Beverage {

    private BeverageType type;
    private Map<IngredientType, Integer> ingredientToQuantityMap;

}
