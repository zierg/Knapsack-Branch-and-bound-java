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
        this(new HashSet<>(), new HashSet<>(), new ArrayList<>());
    }

    public Subtask(Set<Integer> forbiddenClasses, Set<Item> forbiddenItems, Collection<Item> itemsInKnapsack)
    {
        this.forbiddenClasses = forbiddenClasses;
        this.forbiddenItems = forbiddenItems;
        this.itemsInKnapsack = itemsInKnapsack;
        Collection<Item> bestItems = TaskData.getBestClassItems().stream().filter((item -> !itemsInKnapsack.contains(item))).collect(Collectors.toSet());

        for (Item item : itemsInKnapsack)
        {
            ceilCost += item.getCost();
            totalWeight += item.getWeight();
        }

        double guessItemsCost = 0;
        double guessItemsWeight = 0;
        double bestItemsTotalCost = 0;
        for (Item item : bestItems)
        {
            guessItemsCost += item.getCost();
            guessItemsWeight += item.getWeight();
            //ceilCost += item.getCost();
            bestItemsTotalCost += item.getCost();
        }
        if (guessItemsWeight > 0)
        {
            costToWeightGuess = guessItemsCost/guessItemsWeight;
            ceilCost += costToWeightGuess * (TaskData.getMaxWeight() - totalWeight);
        }
        /*if (bestItemsTotalCost < costToWeightGuess * (TaskData.getMaxWeight() - totalWeight))
        {
            System.out.println("---------- ATTENTION ----------");
            System.out.println("bestItemsTotalCost = " + bestItemsTotalCost);
            System.out.println("costToWeightGuess * ... = " + costToWeightGuess * (TaskData.getMaxWeight() - totalWeight));
            System.out.println(String.format("Items in knapsack = %s", itemsInKnapsack));
            System.out.println(String.format("Best items = %s", bestItems));
            System.out.println(String.format("Forbidden classes = %s", forbiddenClasses));
            System.out.println(String.format("Forbidden items = %s", forbiddenItems));
        }*/
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
        else if (prepareChildrenSubtasks())
        {
            execute();
        }
    }

    private void findAccurateSolution()
    {
        findAccurateSolution(new HashSet<>(), 0);
    }

    private void findAccurateSolution(Collection<Item> pickedItems, double pickedWeight)
    {

        ItemsContainer items = TaskData.getItemsContainer();
        Collection<Item> allowedItems = items.getAllowedItems(forbiddenClasses);
        int classId = allowedItems.iterator().next().getClassId();
        Collection<Item> classItems = items.getItemsOfClass(classId);

        boolean lastClass = TaskData.getClassesAmount() - forbiddenClasses.size() == 1;
        if (lastClass)
        {
            Item bestItem = null;
            for (Item item : classItems)
            {
                if ((bestItem == null || item.getCost() > bestItem.getCost()) && totalWeight + pickedWeight + item.getWeight() <= TaskData.getMaxWeight())
                {
                    bestItem = item;
                }
            }
            
            if (bestItem == null)
            {
                return;
            }
            Set<Item> knapsackPickedItems = new HashSet<>(itemsInKnapsack);
            knapsackPickedItems.addAll(pickedItems);
            knapsackPickedItems.add(bestItem);
            TaskData.considerSolution(new Solution(knapsackPickedItems));
            return;
        }

        forbiddenClasses.add(classId);
        for (Item item : classItems)
        {
            pickedItems.add(item);
            findAccurateSolution(pickedItems, pickedWeight + item.getWeight());
            pickedItems.remove(item);
        }
        forbiddenClasses.remove(classId);
    }

    /**
     * Create left subtask and modify current subtask into right subtask
     * @return true in successful case (item for separation has been found), false in other case
     */
    private boolean prepareChildrenSubtasks()
    {
        ItemsContainer items = TaskData.getItemsContainer();
        Item item = items.getBestItem(forbiddenClasses, forbiddenItems, TaskData.getMaxWeight() - totalWeight);
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
        totalWeight += item.getWeight();
    }

    public double getCeilCost()
    {
        return ceilCost;
    }

    private final Set<Integer> forbiddenClasses;
    private final Set<Item> forbiddenItems;

    private double ceilCost;
    private double totalWeight;

    private Collection<Item> itemsInKnapsack;

    private double costToWeightGuess;

    private static final int MIN_CLASSES_AMOUNT = 2;
}
