package knapsack.task;

import knapsack.entities.Item;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import static knapsack.Logger.log;

public class Subtask
{

    public Subtask()
    {
        this(new HashSet<>(), new ArrayList<>(), 0, 0);
    }

    public Subtask(Set<Item> forbiddenItems, Collection<Item> itemsInKnapsack, double knapsackCost, double totalWeight)
    {
        log("Subtask creation started");
        this.forbiddenItems = forbiddenItems;
        this.itemsInKnapsack = itemsInKnapsack;
        log("forbidden = %s", forbiddenItems);
        log("knapsack = %s", itemsInKnapsack);
        long startTime = System.currentTimeMillis();
        bestItems = CommonData.getItemsContainer().getBestItems(forbiddenItems, itemsInKnapsack);
        long finishTime = System.currentTimeMillis();
        logTime("getBestItems time: ", startTime, finishTime);
        log("Expensive items = %s", bestItems);
        if (bestItems.isEmpty())
        {
            solved = true;
            return;
        }

        this.knapsackCost = knapsackCost;
        this.totalWeight = totalWeight;

        this.ceilCost = knapsackCost;
        double itemsWeight = 0;
        startTime = System.currentTimeMillis();
        for (Item item : bestItems)
        {
            if (totalWeight + itemsWeight + item.getWeight() <= CommonData.getMaxWeight())
            {
                itemsWeight += item.getWeight();
                ceilCost += item.getCost();
            }
            else
            {
                double fit = CommonData.getMaxWeight() - totalWeight - itemsWeight;
                ceilCost += item.getCost() * fit / item.getWeight();
                break;
            }
        }
        finishTime = System.currentTimeMillis();
        logTime("Ceil calculation time: ", startTime, finishTime);
    }

    public void execute()
    {
        do
        {
            if (solved || totalWeight == CommonData.getMaxWeight())
            {
                CommonData.considerSolution(new Solution(this.itemsInKnapsack));
                return;
            }
        }
        while (prepareChildrenSubtasks());
        CommonData.considerSolution(new Solution(itemsInKnapsack));
    }

    public double getCeilCost()
    {
        return ceilCost;
    }

    /**
     * Create left subtask and modify current subtask into right subtask
     * @return true in successful case (item for separation has been found), false in other case
     */
    private boolean prepareChildrenSubtasks()
    {
        long startTime = System.currentTimeMillis();
        Item item = getBestItem();
        long finishTime = System.currentTimeMillis();
        logTime("getBestItem time: ", startTime, finishTime);
        log("Selected best item = %s", item);
        if (item == null)
        {
            return false;
        }
        startTime = System.currentTimeMillis();
        createLeftSubtask(item);
        finishTime = System.currentTimeMillis();
        logTime("left subtask creation time: ", startTime, finishTime);
        startTime = System.currentTimeMillis();
        modifyToRightSubtask(item);
        finishTime = System.currentTimeMillis();
        logTime("right subtask modification time: ", startTime, finishTime);
        return true;
    }

    private void createLeftSubtask(Item item)
    {
        Set<Item> leftSubtaskForbiddenItems = new HashSet<>(forbiddenItems);
        Set<Item> leftSubtaskItemsInKnapsack = new HashSet<>(itemsInKnapsack);
        leftSubtaskForbiddenItems.add(item);
        Subtask leftSubtask = new Subtask(leftSubtaskForbiddenItems, leftSubtaskItemsInKnapsack, knapsackCost, totalWeight);
        CommonData.addSubtask(leftSubtask);
    }

    private void modifyToRightSubtask(Item item)
    {
        itemsInKnapsack.add(item);
        knapsackCost += item.getCost();
        totalWeight += item.getWeight();
    }

    private Item getBestItem()
    {
        ListIterator<Item> iterator = bestItems.listIterator();
        while (iterator.hasNext())
        {
            Item item = iterator.next();
            iterator.remove();
            if (item.getWeight() <= CommonData.getMaxWeight() - totalWeight)
            {
                return item;
            }
        }
        return null;
    }

    private void logTime(String message, long start, long finish)
    {
        //System.out.println(message + (finish - start));
    }

    private final Set<Item> forbiddenItems;

    private double ceilCost = 0;
    private double totalWeight;
    private double knapsackCost;

    private final Collection<Item> itemsInKnapsack;
    private final List<Item> bestItems;

    private boolean solved = false;
}
