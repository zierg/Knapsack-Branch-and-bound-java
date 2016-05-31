package knapsack.task;

import knapsack.entities.Item;
import knapsack.entities.ItemsContainer;

import java.io.FileInputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class TaskData
{
    public static ItemsContainer getItemsContainer()
    {
        return itemsContainer;
    }

    public static int getClassesAmount()
    {
        return classesAmount;
    }

    public static double getMaxWeight()
    {
        return maxWeight;
    }

    public static void addSubtask(Subtask subtask)
    {
        subtasks.add(subtask);
    }

    public static boolean doSubtaskExist()
    {
        return !subtasks.isEmpty();
    }

    public static Subtask getBestSubtask()
    {
        Subtask best = subtasks.iterator().next();
        subtasks.remove(best);
        return best;
    }

    public static Solution getBestSolution()
    {
        return bestSolution;
    }

    public static void considerSolution(Solution solution)
    {
        //System.out.println("consider");
        if (bestSolution == null || solution.getCost() > bestSolution.getCost())
        {
            System.out.println("found new best!");
            bestSolution = solution;
            Set<Subtask> bad = subtasks.stream().filter((subtask -> subtask.getCeilCost() <= solution.getCost())).collect(Collectors.toSet());
            subtasks.removeAll(bad);
        }
    }

    public static Collection<Item> getBestClassItems()
    {
        return bestClassItems;
    }

    public static Map<Integer, Item> getBestClassItemsMap()
    {
        return bestClassItemsMap;
    }

    /**
     * The structure of the file should be as following
     * MAX_WEIGHT=%some double value%
     * ITEMS=class1 cost1 weight1;class2 cost2 weight2;class3 cost3 weight3 ...
     *
     * @param filename filename
     */
    public static void load(String filename)
    {
        try (FileInputStream input = new FileInputStream(filename))
        {
            Properties properties = new Properties();
            properties.load(input);
            maxWeight = Double.parseDouble(properties.getProperty("MAX_WEIGHT"));
            classesAmount = Integer.parseInt(properties.getProperty("CLASS_AMOUNT"));

            StringTokenizer itemTokenizer = new StringTokenizer(properties.getProperty("ITEMS"), ";");
            ItemsContainer.ItemsContainerBuilder builder = ItemsContainer.builder();

            double minCostToWeight = Double.MAX_VALUE;
            double minWeightAndCapacity = Double.MAX_VALUE;
            double maxCostToWeight = - Double.MAX_VALUE;
            double maxWeightAndCapacity = - Double.MAX_VALUE;

            int itemsAmount = 0;
            double totalCost = 0;

            while (itemTokenizer.hasMoreTokens())
            {
                itemsAmount++;
                StringTokenizer parameters = new StringTokenizer(itemTokenizer.nextToken(), " ");
                int classId = Integer.parseInt(parameters.nextToken());
                double cost = Double.parseDouble(parameters.nextToken());
                totalCost += cost;
                double weight = Double.parseDouble(parameters.nextToken());
                Item newItem = new Item(classId, weight, cost, classesAmount);
                builder.addItem(newItem);

                if (newItem.getCostToWeight() < minCostToWeight) { minCostToWeight = newItem.getCostToWeight(); }
                if (newItem.getCostToWeight() > maxCostToWeight) { maxCostToWeight = newItem.getCostToWeight(); }
                if (newItem.getWeightAndCapacity() < minWeightAndCapacity) { minWeightAndCapacity = newItem.getWeightAndCapacity(); }
                if (newItem.getWeightAndCapacity() > maxWeightAndCapacity) { maxWeightAndCapacity = newItem.getWeightAndCapacity(); }

                Item currentBestItem = bestClassItemsMap.get(classId);
                if (currentBestItem == null || currentBestItem.getCost() < newItem.getCost())
                {
                    bestClassItemsMap.put(classId, newItem);
                }
            }

            double averageCost = totalCost / (double) itemsAmount;
            itemsContainer = builder.build(minCostToWeight, maxCostToWeight, minWeightAndCapacity, maxWeightAndCapacity);
            bestClassItems = bestClassItemsMap.values();
            subtasks.add(new Subtask());
        }
        catch (Exception ex)
        {
            throw new RuntimeException(ex);
        }
    }

    private static ItemsContainer itemsContainer;
    private static double maxWeight;
    private static int classesAmount;
    private static Solution bestSolution;

    private static final Set<Subtask> subtasks = new TreeSet<>((task1, task2) -> Double.compare(task2.getCeilCost(), task1.getCeilCost()));
    private static final Map<Integer, Item> bestClassItemsMap = new HashMap<>();
    private static Collection<Item> bestClassItems;

    private TaskData() {}
}
