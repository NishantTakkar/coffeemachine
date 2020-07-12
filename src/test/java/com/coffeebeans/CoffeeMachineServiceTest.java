package com.coffeebeans;

import com.coffeebeans.enums.BeverageType;
import com.coffeebeans.enums.IngredientType;
import com.coffeebeans.request.OrderRequestDTO;
import com.coffeebeans.service.CoffeeMachineService;
import com.coffeebeans.utils.ReaderUtil;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.coffeebeans.service.CoffeeMachineService.bootStrapCoffeeMachine;

public class CoffeeMachineServiceTest {

    private CoffeeMachineService coffeeMachineService;


    @Test
    public void shouldHandleNParallelNBeveragesTest() throws FileNotFoundException, InterruptedException {

        OrderRequestDTO orderRequest = ReaderUtil.getOrderRequest(ReaderUtil.getJsonString(new File("/Users/nishanttakkar/Documents/work/workspace/coffee-machine/src/resources/coffee-machine-input-1.json")));
        coffeeMachineService = bootStrapCoffeeMachine(orderRequest);

        final int numberOfOutlets = orderRequest.machine.getOutlets().getCount_n();

        List<BeverageType> beverages = Arrays.asList(BeverageType.black_tea, BeverageType.hot_coffee, BeverageType.green_tea);
        for (int i = 0; i < numberOfOutlets; i++) {
                coffeeMachineService.submitBeverageRequest(beverages.get(i));
        }
        Thread.sleep(2000);

        Map<IngredientType, Integer> ingredientsWithRemainingQuantity = coffeeMachineService.getAvailableInventory();

        System.out.println("Ingredients with remaining quantity");
        for (IngredientType type : ingredientsWithRemainingQuantity.keySet()) {
            System.out.println(type.name() + " : " + ingredientsWithRemainingQuantity.get(type));
        }

    }

    @Test
    public void shouldhandleAnyBeveragesRequested() throws FileNotFoundException, InterruptedException {

        OrderRequestDTO orderRequest = ReaderUtil.getOrderRequest(ReaderUtil.getJsonString(new File("/Users/nishanttakkar/Documents/work/workspace/coffee-machine/src/resources/coffee-machine-input-1.json")));
        coffeeMachineService = bootStrapCoffeeMachine(orderRequest);

        List<BeverageType> beverages = Arrays.asList(BeverageType.black_tea, BeverageType.hot_coffee, BeverageType.green_tea, BeverageType.black_tea, BeverageType.hot_coffee, BeverageType.green_tea, BeverageType.black_tea, BeverageType.hot_coffee, BeverageType.green_tea, BeverageType.green_tea);
        for (int i = 0; i < beverages.size(); i++) {
            coffeeMachineService.submitBeverageRequest(beverages.get(i));
        }
        Thread.sleep(2000);

        Map<IngredientType, Integer> ingredientsWithRemainingQuantity = coffeeMachineService.getAvailableInventory();
        System.out.println();
        System.out.println("Ingredients with remaining quantity are:");
        for (IngredientType type : ingredientsWithRemainingQuantity.keySet()) {
            System.out.println(type.name() + " : " + ingredientsWithRemainingQuantity.get(type));
        }
    }

    @Test
    public void testLowInventoryAlerts() throws FileNotFoundException {

        OrderRequestDTO orderRequest = ReaderUtil.getOrderRequest(ReaderUtil.getJsonString(new File("/Users/nishanttakkar/Documents/work/workspace/coffee-machine/src/resources/coffee-machine-input-1.json")));
        coffeeMachineService = bootStrapCoffeeMachine(orderRequest);
        Map<IngredientType, Integer> lowInventoryIngredients = coffeeMachineService.getLowInventoryIngredients();
        System.out.println();
        System.out.println("Ingredients with Low quantity are:");
        for (IngredientType type : lowInventoryIngredients.keySet()) {
            System.out.println(type.name() + " : " + lowInventoryIngredients.get(type));
        }

    }
}
