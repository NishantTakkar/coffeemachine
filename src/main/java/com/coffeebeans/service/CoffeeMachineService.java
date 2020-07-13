package com.coffeebeans.service;

import com.coffeebeans.entity.Beverage;

import com.coffeebeans.enums.BeverageType;
import com.coffeebeans.enums.IngredientType;
import com.coffeebeans.request.OrderRequestDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class CoffeeMachineService {


    private InventoryService inventoryService;
    private BeverageConfigService beverageConfigService;
    private static CoffeeMachineService MACHINE_INSTANCE;
    private BlockingQueue<BeverageType> beverageBlockingQueue;



    public static CoffeeMachineService bootStrapCoffeeMachine(OrderRequestDTO orderRequest) {

        //passing Total Ingredient Quantity to Inventory Service
        InventoryService inventoryService = new InventoryService(orderRequest.machine.getTotal_items_quantity());

        //Passing Beverage Ingredient Details to Beverage Service
        BeverageConfigService beverageConfigService = new BeverageConfigService(orderRequest.machine.getBeverages());

        CoffeeMachineService coffeeMachine = CoffeeMachineService.getInstance();

        coffeeMachine.inventoryService = inventoryService;

        coffeeMachine.beverageConfigService = beverageConfigService;

        //set size of blocking queue as outlet size to make sure N number of outlets are processing parallel
        coffeeMachine.beverageBlockingQueue=new ArrayBlockingQueue<>(orderRequest.machine.getOutlets().getCount_n());
        coffeeMachine.processBeverage();
        return coffeeMachine;

    }

    static CoffeeMachineService getInstance() {
        if (MACHINE_INSTANCE == null) {
            synchronized (CoffeeMachineService.class) {
                if (MACHINE_INSTANCE == null) {
                    MACHINE_INSTANCE = new CoffeeMachineService();
                }
            }
        }
        return MACHINE_INSTANCE;
    }

    public void submitBeverageRequest(BeverageType type){
        new Thread(() -> {
            try {
                beverageBlockingQueue.put(type);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }


    public void processBeverage(){
        new Thread(() -> {
            while (true){
                try {
                    System.out.println(makeBeverage(beverageBlockingQueue.take()));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }).start();
    }



    public String makeBeverage(BeverageType beverageType) {

        Beverage beverage = beverageConfigService.getBeverageConfig(beverageType);

        List<IngredientType> unavailableIngredients = new ArrayList<>();

        for (Map.Entry<IngredientType, Integer> entry : beverage.getIngredientToQuantityMap().entrySet()) {

            if (!inventoryService.checkAvailability(entry.getKey(), entry.getValue())) {
                unavailableIngredients.add(entry.getKey());
            }

        }

        if (!unavailableIngredients.isEmpty()) {
            StringBuilder stringBuilder=new StringBuilder();
            stringBuilder.append("Sorry. We have ");
            unavailableIngredients.forEach(it -> stringBuilder.append(it).append(" ,"));
            return stringBuilder.append(" in Insufficient Quantity.We Cannot serve ").append(beverageType.name()).toString();
        }

        for (Map.Entry<IngredientType, Integer> entry : beverage.getIngredientToQuantityMap().entrySet()) {
            inventoryService.blockInventory(entry.getKey(), entry.getValue());
        }


        return "Prepared => "+ beverageType.name();

    }

    public Map<IngredientType, Integer> getLowInventoryIngredients(){
        return inventoryService.getAllIngredientsWithLowQuantity();
    }

    public Map<IngredientType, Integer> getAvailableInventory(){
        return inventoryService.getAvailableInventory();
    }


}
