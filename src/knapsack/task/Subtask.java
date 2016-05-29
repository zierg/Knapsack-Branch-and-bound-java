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
    }

    public void execute()
    {

        if (TaskData.getClassesAmount() - forbiddenClasses.size() <= MIN_CLASSES_AMOUNT)
        {
            // TODO: findAccurateSolution();
        }
        else
        {
            prepareChildrenSubtasks();
            execute();
        }
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
        ceilCost += item.getCost() - TaskData.getBestClassItemsMap().get(item.getClassId()).getCost();
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

    private Collection<Item> itemsInKnapsack;

    private static final int MIN_CLASSES_AMOUNT = 3;
}
