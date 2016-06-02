package knapsack.task;

import knapsack.entities.Item;
import knapsack.entities.ItemsContainer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static knapsack.Logger.log;

public class Subtask
{

    public Subtask()
    {
        this(new HashSet<>(), new ArrayList<>());
    }

    public Subtask(Set<Item> forbiddenItems, Collection<Item> itemsInKnapsack)
    {
        log("Subtask creation started");
        this.forbiddenItems = forbiddenItems;
        this.itemsInKnapsack = itemsInKnapsack;
        log("forbidden = %s", forbiddenItems);
        log("knapsack = %s", itemsInKnapsack);
        Collection<Item> bestItems = TaskData.getItemsContainer().getBestItems(forbiddenItems, itemsInKnapsack);
        log("Expensive items = %s", bestItems);
        if (bestItems.isEmpty())
        {
            TaskData.considerSolution(new Solution(this.itemsInKnapsack));
            solved = true;
            return;
        }

        for (Item item : itemsInKnapsack)
        {
            ceilCost += item.getCost();
            totalWeight += item.getWeight();
        }

        double itemsWeight = 0;
        for (Item item : bestItems)
        {
            if (totalWeight + itemsWeight + item.getWeight() <= TaskData.getMaxWeight())
            {

                itemsWeight += item.getWeight();
                ceilCost += item.getCost();
            }
            else
            {
                double fit = TaskData.getMaxWeight() - totalWeight - itemsWeight;

                ceilCost += item.getCost() * fit / item.getWeight();
                break;
            }

        }
    }

    public void execute()
    {
        if (solved || totalWeight > TaskData.getMaxWeight())
        {
            return;
        }
        if (totalWeight == TaskData.getMaxWeight())
        {
            TaskData.considerSolution(new Solution(this.itemsInKnapsack));
            return;
        }

        if (prepareChildrenSubtasks())
        {
            execute();
        }
        else
        {
            TaskData.considerSolution(new Solution(itemsInKnapsack));
        }
    }

    private void findAccurateSolution()
    {
        findAccurateSolution(new HashSet<>(), 0);
    }

    private void findAccurateSolution(Collection<Item> pickedItems, double pickedWeight)
    {
        ItemsContainer items = TaskData.getItemsContainer();
        double weightLimit = TaskData.getMaxWeight() - totalWeight - pickedWeight;
        Collection<Item> allowedItems = items.getAllowedItems(forbiddenItems, pickedItems, weightLimit);
        if (allowedItems.isEmpty())
        {
            return;
        }

        boolean lastItem = allowedItems.size() == 1;
        if (lastItem)
        {
            Item bestItem = allowedItems.iterator().next();
            Set<Item> knapsackPickedItems = new HashSet<>(itemsInKnapsack);
            knapsackPickedItems.addAll(pickedItems);
            knapsackPickedItems.add(bestItem);
            TaskData.considerSolution(new Solution(knapsackPickedItems));
            return;
        }

        for (Item item : allowedItems)
        {
            pickedItems.add(item);
            findAccurateSolution(pickedItems, pickedWeight + item.getWeight());
            pickedItems.remove(item);
        }
    }

    /**
     * Create left subtask and modify current subtask into right subtask
     * @return true in successful case (item for separation has been found), false in other case
     */
    private boolean prepareChildrenSubtasks()
    {
        ItemsContainer items = TaskData.getItemsContainer();
        Item item = items.getBestItem(forbiddenItems, itemsInKnapsack, TaskData.getMaxWeight() - totalWeight);
        log("Selected best item = %s", item);
        if (item == null)
        {
            return false;
        }
        createLeftSubtask(item);
        modifyToRightSubtask(item);
        return true;
    }

    private void createLeftSubtask(Item item)
    {
        Set<Item> leftSubtaskForbiddenItems = new HashSet<>(forbiddenItems);
        Set<Item> leftSubtaskItemsInKnapsack = new HashSet<>(itemsInKnapsack);
        leftSubtaskForbiddenItems.add(item);
        Subtask leftSubtask = new Subtask(leftSubtaskForbiddenItems, leftSubtaskItemsInKnapsack);
        TaskData.addSubtask(leftSubtask);
    }

    private void modifyToRightSubtask(Item item)
    {
        itemsInKnapsack.add(item);
        totalWeight += item.getWeight();
    }

    public double getCeilCost()
    {
        return ceilCost;
    }

    private final Set<Item> forbiddenItems;

    private double ceilCost = 0;
    private double totalWeight;

    private final Collection<Item> itemsInKnapsack;

    private boolean solved = false;

    private static final int MIN_ITEMS_AMOUNT = 4;
}
