package knapsack.task;

import knapsack.entities.Item;

import java.util.Collection;

public class Solution
{

    public Solution(Collection<Item> items)
    {
        this.items = items;
        double currentCost = 0;
        for (Item item : items)
        {
            currentCost += item.getCost();
        }
        cost = currentCost;
    }

    public double getCost()
    {
        return cost;
    }

    public Collection<Item> getItems()
    {
        return items;
    }

    private final double cost;

    private final Collection<Item> items;
}
