package knapsack.task;

import knapsack.entities.Item;
import knapsack.entities.ItemsContainer;

import java.io.FileInputStream;
import java.util.Properties;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeSet;
import java.util.stream.Collectors;

import static knapsack.Logger.log;
public class CommonData
{
    public static ItemsContainer getItemsContainer()
    {
        return itemsContainer;
    }

    public static int getBadSolutionsAmount()
    {
        return badSolutionsAmount;
    }

    public static int getGoodSolutionsAmount()
    {
        return goodSolutionsAmount;
    }

    public static double getMaxWeight()
    {
        return maxWeight;
    }

    public static void addSubtask(Subtask subtask)
    {
        log("Adding subtask. Ceil = %s", subtask.getCeilCost());
        if (bestSolution == null || subtask.getCeilCost() > bestSolution.getCost())
        {
            log("Added.");
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
        log("Next subtask. Ceil = %s", best.getCeilCost());
        subtasks.remove(best);
        return best;
    }

    public static Solution getBestSolution()
    {
        return bestSolution;
    }

    public static void considerSolution(Solution solution)
    {
        log("Considering. Cost = %s", solution.getCost());
        log("Curren best = %s", bestSolution == null ? null : bestSolution.getCost());
        if (bestSolution == null || solution.getCost() > bestSolution.getCost())
        {
            bestSolution = solution;
            if (subtasks.isEmpty())
            {
                return;
            }
            Set<Subtask> bad = subtasks.stream().filter((subtask -> subtask.getCeilCost() <= solution.getCost())).collect(Collectors.toSet());
            subtasks.removeAll(bad);
            goodSolutionsAmount++;
        }
        else
        {
            badSolutionsAmount++;
        }
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

            StringTokenizer itemTokenizer = new StringTokenizer(properties.getProperty("ITEMS"), ";");
            ItemsContainer.ItemsContainerBuilder builder = ItemsContainer.builder();

            while (itemTokenizer.hasMoreTokens())
            {
                StringTokenizer parameters = new StringTokenizer(itemTokenizer.nextToken(), " ");
                int limit = Integer.parseInt(parameters.nextToken());
                double cost = Double.parseDouble(parameters.nextToken());
                double weight = Double.parseDouble(parameters.nextToken());
                for (int i = 0; i < limit; i++)
                {
                    Item newItem = new Item(weight, cost, i == 0);
                    builder.addItem(newItem);
                }
            }

            itemsContainer = builder.build();
            subtasks.add(new Subtask());
        }
        catch (Exception ex)
        {
            throw new RuntimeException(ex);
        }
    }

    private static ItemsContainer itemsContainer;
    private static double maxWeight;
    private static Solution bestSolution;

    private static final Set<Subtask> subtasks = new TreeSet<>((task1, task2) -> Double.compare(task2.getCeilCost(), task1.getCeilCost()));

    private static int goodSolutionsAmount = 0;
    private static int badSolutionsAmount = 0;

    private CommonData() {}
}
