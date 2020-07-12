package com.coffeebeans.service;

import com.coffeebeans.enums.IngredientType;
import com.coffeebeans.exceptions.NotAllowedException;
import com.coffeebeans.exceptions.NotFoundException;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InventoryService {


    private Map<IngredientType, Integer> ingredientInventoryMap=new ConcurrentHashMap<>();
    private static  final Integer LOW_INVENTORY_COUNT=101;

    public InventoryService(Map<IngredientType, Integer> ingredientInventoryMap) {
       this.ingredientInventoryMap.putAll(ingredientInventoryMap);
    }

    public void blockInventory(IngredientType type, Integer qty) {


        if (ingredientInventoryMap.containsKey(type)) {

            synchronized (type.name()) {
                Integer remainingQuantity = ingredientInventoryMap.get(type) - qty;
                if (remainingQuantity < 0) {
                    throw new NotAllowedException("Required qty of" + type.name() + "is not available");
                }
                ingredientInventoryMap.put(type,remainingQuantity);
            }
            return;
        }
        throw new NotFoundException("Ingredient "+ type.name());
    }

    boolean checkAvailability(IngredientType type,  Integer qty) {

        if (ingredientInventoryMap.containsKey(type) && ingredientInventoryMap.get(type) > qty ) {
            return true;
        }
        return false;
    }

    public Map<IngredientType, Integer> getAllIngredientsWithLowQuantity() {
        Map<IngredientType, Integer> ingredientsWithLowQuantity = new HashMap<>();
        for (IngredientType type : ingredientInventoryMap.keySet()) {
            if (ingredientInventoryMap.get(type) < LOW_INVENTORY_COUNT) {
                ingredientsWithLowQuantity.put(type,ingredientInventoryMap.get(type));
            }
        }
        return ingredientsWithLowQuantity;
    }

    public Map<IngredientType, Integer> getAvailableInventory() {
        return ingredientInventoryMap;
    }




}
