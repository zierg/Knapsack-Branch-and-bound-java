package knapsack;

import knapsack.entities.Item;
import knapsack.task.CommonData;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public class BruteForce
{
    public static void main(String[] args) throws Exception
    {
        CommonData.load("task.ini");
        maxWeight = CommonData.getMaxWeight();
        Collection<Item> items = CommonData.getItemsContainer().getBestItems(new HashSet<>(), new HashSet<>());
        System.out.println("items: ");
        printThings(items);
        solutionWeight = 0;
        long startTime = System.currentTimeMillis();
        findSolution(items, new HashSet<>(), 0);
        long finishTime = System.currentTimeMillis();
        System.out.println("Brute-force.");

        printThings(solutionThings);

        System.out.println(String.format("Bag capacity = %s", maxWeight));
        System.out.println("Solution weight = " + solutionWeight);
        System.out.println(String.format("Total cost: %s", solutionCost));
        System.out.println(String.format("Spent time: %s ms", finishTime - startTime));
    }

    private static void printThings(Collection<Item> items)
    {
        List<Item> sorted = items.stream().sorted((item1, item2) -> Integer.compare(item1.getRealId(), item2.getRealId())).collect(Collectors.toList());
        Item previousItem = sorted.get(0);
        int itemAmount = 0;
        int previousRealId = previousItem.getRealId();
        for (Item item : sorted)
        {
            solutionWeight += item.getWeight();
            if (item.getRealId() == previousRealId)
            {
                itemAmount++;
                continue;
            }
            printItem(previousItem, itemAmount);
            itemAmount = 1;
            previousRealId = item.getRealId();
            previousItem = item;
        }
    }

    private static void printItem(Item item, int amount)
    {
        System.out.println(String.format("Item %s (%s): cost = %s, weight = %s. Total cost = %s, weight = %s"
                , item.getRealId()
                , amount
                , item.getCost()
                , item.getWeight()
                , item.getCost() * (double) amount
                , item.getWeight() * (double) amount
        ));
    }

    private static void findSolution(Collection<Item> items, Collection<Item> picked, double totalWeight)
    {
        if (found)
        {
            return;
        }
        if (items.isEmpty())
        {
            solutionThings = new HashSet<>(picked);
            solutionCost = calculateCost(picked);
            found = true;
            return;
        }
        Collection<Item> allowedItems =
                items.stream()
                        .filter((thing -> (maxWeight - totalWeight) >= thing.getWeight())).collect(Collectors.toList());
        if (allowedItems.isEmpty())
        {
            double currentCost = calculateCost(picked);
            if (currentCost > solutionCost)
            {
                solutionThings = new HashSet<>(picked);
                solutionCost = currentCost;
            }

            return;
        }
        allowedItems.forEach(item ->
                             {
                                 items.remove(item);
                                 picked.add(item);
                                 findSolution(items, picked, totalWeight + item.getWeight());
                                 picked.remove(item);
                                 items.add(item);
                             });
    }

    private static double calculateCost(Collection<Item> items)
    {
        double cost = 0;
        for (Item item : items)
        {
            cost += item.getCost();
        }
        return cost;
    }

    private static double maxWeight;
    private static boolean found = false;
    private static Collection<Item> solutionThings = null;
    private static double solutionCost = 0;
    private static double solutionWeight = 0;
}