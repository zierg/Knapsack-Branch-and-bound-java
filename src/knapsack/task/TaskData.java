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

    public static void removeSubtask(Subtask subtask)
    {
        subtasks.remove(subtask);
    }

    public static Subtask getBestSubtask()
    {
        return subtasks.iterator().next();
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
     * @param filename filename
     */
    public static void load(String filename)
    {
        try (FileInputStream input = new FileInputStream(filename))
        {
            Properties properties = new Properties();
            properties.load(input);
            maxWeight = Double.parseDouble(properties.getProperty("MAX_WEIGHT"));

            StringTokenizer itemTokenizer = new StringTokenizer(properties.getProperty("ITEMS"), ";");
            ItemsContainer.ItemsContainerBuilder builder = ItemsContainer.builder();

            Set<Integer> classes = new HashSet<>();

            while (itemTokenizer.hasMoreTokens())
            {
                StringTokenizer parameters = new StringTokenizer(itemTokenizer.nextToken(), " ");
                int classId = Integer.parseInt(parameters.nextToken());
                double cost = Double.parseDouble(parameters.nextToken());
                double weight = Double.parseDouble(parameters.nextToken());
                Item newItem = new Item(classId, weight, cost);
                builder.addItem(newItem);
                classes.add(classId);

                Item currentBestItem = bestClassItemsMap.get(classId);
                if (currentBestItem == null || currentBestItem.getCost() < newItem.getCost())
                {
                    bestClassItemsMap.put(classId, newItem);
                }
            }

            itemsContainer = builder.build();
            classesAmount = classes.size();
            bestClassItems = bestClassItemsMap.values();
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
