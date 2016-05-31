package knapsack.task;

import javafx.util.Pair;
import knapsack.entities.Item;
import knapsack.entities.ItemsContainer;

import java.io.FileInputStream;
import java.util.Collection;
import java.util.HashMap;
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

    public static int getBadSolutionsAmount()
    {
        return badSolutionsAmount;
    }

    public static void setBadSolutionsAmount(int badSolutionsAmount)
    {
        TaskData.badSolutionsAmount = badSolutionsAmount;
    }

    public static int getGoodSolutionsAmount()
    {
        return goodSolutionsAmount;
    }

    public static void setGoodSolutionsAmount(int goodSolutionsAmount)
    {
        TaskData.goodSolutionsAmount = goodSolutionsAmount;
    }

    public static double getMaxWeight()
    {
        return maxWeight;
    }

    public static void addSubtask(Subtask subtask)
    {
        if (bestSolution == null || subtask.getCeilCost() > bestSolution.getCost())
        {
            subtasks.add(subtask);
        }
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
        if (bestSolution == null || solution.getCost() > bestSolution.getCost())
        {
            bestSolution = solution;
            Set<Subtask> bad = subtasks.stream().filter((subtask -> subtask.getCeilCost() <= solution.getCost())).collect(Collectors.toSet());
            System.out.println("Items = " + solution.getItems().stream().sorted((item1, item2) -> Integer.compare(item1.getClassId(), item2.getClassId())).collect(Collectors.toList()));
            System.out.println("Cost = " + solution.getCost());
            System.out.println("Bad size = " + bad.size());
            System.out.println("Current bad solutions = " + badSolutionsAmount);
            subtasks.removeAll(bad);
            goodSolutionsAmount++;
        }
        else
        {
            badSolutionsAmount++;
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
            double minCost = Double.MAX_VALUE;
            double minWeightAndCapacity = Double.MAX_VALUE;
            double maxCostToWeight = - Double.MAX_VALUE;
            double maxCost = - Double.MAX_VALUE;
            double maxWeightAndCapacity = - Double.MAX_VALUE;

            Map<Pair<Integer, Double>, Double> bestClassCosts = new HashMap<>();

            while (itemTokenizer.hasMoreTokens())
            {
                StringTokenizer parameters = new StringTokenizer(itemTokenizer.nextToken(), " ");
                int classId = Integer.parseInt(parameters.nextToken());
                double cost = Double.parseDouble(parameters.nextToken());
                double weight = Double.parseDouble(parameters.nextToken());

                Pair<Integer, Double> pair = new Pair<>(classId, cost);
                if (bestClassCosts.containsKey(pair) && bestClassCosts.get(pair) <= weight)
                {
                    continue;
                }
                bestClassCosts.put(pair, weight);

                Item newItem = new Item(classId, weight, cost, classesAmount);
                builder.addItem(newItem);

                if (newItem.getCostToWeight() < minCostToWeight) { minCostToWeight = newItem.getCostToWeight(); }
                if (newItem.getCostToWeight() > maxCostToWeight) { maxCostToWeight = newItem.getCostToWeight(); }
                if (cost < minCost) { minCost = cost; }
                if (cost > maxCost) { maxCost = cost; }
                if (newItem.getWeightAndCapacity() < minWeightAndCapacity) { minWeightAndCapacity = newItem.getWeightAndCapacity(); }
                if (newItem.getWeightAndCapacity() > maxWeightAndCapacity) { maxWeightAndCapacity = newItem.getWeightAndCapacity(); }

                Item currentBestItem = bestClassItemsMap.get(classId);
                if (currentBestItem == null || currentBestItem.getCost() < newItem.getCost())
                {
                    bestClassItemsMap.put(classId, newItem);
                }
            }

            itemsContainer = builder.build(minCostToWeight, maxCostToWeight, minWeightAndCapacity, maxWeightAndCapacity, minCost, maxCost);
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

    private static int goodSolutionsAmount = 0;
    private static int badSolutionsAmount = 0;

    private TaskData() {}
}
