package com.coffeebeans.service;

import com.coffeebeans.entity.Beverage;
import com.coffeebeans.enums.BeverageType;
import com.coffeebeans.enums.IngredientType;
import com.coffeebeans.exceptions.NotFoundException;

import java.util.HashMap;
import java.util.Map;

public class BeverageConfigService {

    private Map<BeverageType, Beverage> beverageMap;

    public BeverageConfigService(Map<BeverageType, Map<IngredientType, Integer>> beverages) {
        beverageMap=new HashMap<>();
        beverages.forEach((key, value) -> beverageMap.put(key, new Beverage(key, value)));
    }

    public Beverage getBeverageConfig(BeverageType type) {
        if (beverageMap.containsKey(type)) {
            return beverageMap.get(type);
        }
        throw new NotFoundException("Beverage Not Present");
    }

}
