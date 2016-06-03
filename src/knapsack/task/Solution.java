package knapsack.task;

import knapsack.entities.Item;

import java.util.Collection;

public class Solution
{

    public Solution(Collection<Item> items)
    {
        this.items = items;
        double currentCost = 0;
        double currentWeight = 0;
        for (Item item : items)
        {
            currentCost += item.getCost();
            currentWeight += item.getWeight();
        }
        cost = currentCost;
        weight = currentWeight;
    }

    public double getCost()
    {
        return cost;
    }

    public double getWeight()
    {
        return weight;
    }

    public Collection<Item> getItems()
    {
        return items;
    }

    private final double cost;
    private final double weight;

    private final Collection<Item> items;
}
