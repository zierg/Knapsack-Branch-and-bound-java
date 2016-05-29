package knapsack.task;

import knapsack.entities.Item;
import knapsack.entities.ItemsContainer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class Subtask
{

    public Subtask()
    {
        this(new HashSet<>(), new HashSet<>(), new ArrayList<>(), true);
    }

    public Subtask(Set<Integer> forbiddenClasses, Set<Item> forbiddenItems, Collection<Item> itemsInKnapsack)
    {
        this(forbiddenClasses, forbiddenItems, itemsInKnapsack, true);
    }

    public Subtask(Set<Integer> forbiddenClasses, Set<Item> forbiddenItems, Collection<Item> itemsInKnapsack, boolean calculateCeil)
    {
        this.forbiddenClasses = forbiddenClasses;
        this.forbiddenItems = forbiddenItems;
        this.itemsInKnapsack = itemsInKnapsack;
        Collection<Item> bestItems = TaskData.getBestClassItems().stream().filter((item -> !itemsInKnapsack.contains(item))).collect(Collectors.toSet());
        if (calculateCeil)
        {
            for (Item item : itemsInKnapsack)
            {
                ceilCost += item.getCost();
            }
            for (Item item : bestItems)
            {
                ceilCost += item.getCost();
            }
        }
        for (Item item : itemsInKnapsack)
        {
            totalWeight += item.getWeight();
        }
    }

    public void execute()
    {
        if (totalWeight > TaskData.getMaxWeight())
        {
            return;
        }

        if (TaskData.getClassesAmount() - forbiddenClasses.size() <= MIN_CLASSES_AMOUNT)
        {
            findAccurateSolution();
        }
        else
        {
            prepareChildrenSubtasks();
            execute();
        }
    }

    private void findAccurateSolution()
    {
        findAccurateSolution(new HashSet<Item>(), 0);
    }

    private void findAccurateSolution(Collection<Item> pickedItems, double pickedWeight)
    {
        ItemsContainer items = TaskData.getItemsContainer();
        Collection<Item> allowedItems = items.getAllowedItems(forbiddenClasses);
        int classId = allowedItems.iterator().next().getClassId();
        Collection<Item> currentItems = items.getItemsOfClass(classId);
        boolean lastClass = TaskData.getClassesAmount() - forbiddenClasses.size() == 1;

        if (lastClass)
        {
            Item bestItem = null;
            for (Item item : currentItems)
            {
                if (bestItem == null || (item.getCost() > bestItem.getCost() && totalWeight + pickedWeight + item.getWeight() <= TaskData.getMaxWeight()))
                {
                    bestItem = item;
                }
            }
            Set<Item> knapsackPickedItems = new HashSet<>(pickedItems);
            knapsackPickedItems.add(bestItem);
            TaskData.considerSolution(new Solution(knapsackPickedItems));
            return;
        }

        forbiddenClasses.add(classId);
        for (Item item : currentItems)
        {
            pickedItems.add(item);
            findAccurateSolution(pickedItems, pickedWeight + item.getWeight());
            pickedItems.remove(item);
        }
        forbiddenClasses.remove(classId);
    }

    private boolean arrayContains(int[] array, int key)
    {
        for (int value : array)
        {
            if (value == key)
            {
                return true;
            }
        }
        return false;
    }

    private void prepareChildrenSubtasks()
    {
        ItemsContainer items = TaskData.getItemsContainer();
        Item item = items.getBestItem(forbiddenClasses, forbiddenItems);
        createLeftSubtask(item);
        modifyToRightSubtask(item);
    }

    private void createLeftSubtask(Item item)
    {
        Set<Integer> leftSubtaskClasses = new HashSet<>(forbiddenClasses);
        Set<Item> leftSubtaskForbiddenItems = new HashSet<>(forbiddenItems);
        Set<Item> leftSubtaskItemsInKnapsack = new HashSet<>(itemsInKnapsack);
        leftSubtaskForbiddenItems.add(item);
        Subtask leftSubtask = new Subtask(leftSubtaskClasses, leftSubtaskForbiddenItems, leftSubtaskItemsInKnapsack);
        TaskData.addSubtask(leftSubtask);
    }

    private void modifyToRightSubtask(Item item)
    {
        forbiddenClasses.add(item.getClassId());
        itemsInKnapsack.add(item);
        Item bestClassItem = TaskData.getBestClassItemsMap().get(item.getClassId());
        ceilCost += item.getCost() - bestClassItem.getCost();
        totalWeight += item.getWeight() - bestClassItem.getWeight();

    }

    public double getCeilCost()
    {
        return ceilCost;
    }

    public Collection<Item> getItemsInKnapsack()
    {
        return itemsInKnapsack;
    }

    private final Set<Integer> forbiddenClasses;
    private final Set<Item> forbiddenItems;

    private double ceilCost;
    private double totalWeight;

    private Collection<Item> itemsInKnapsack;

    private static final int MIN_CLASSES_AMOUNT = 3;
}
