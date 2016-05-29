package knapsack;

import knapsack.entities.Item;
import knapsack.entities.ItemsContainer;

import java.io.FileInputStream;
import java.util.Properties;
import java.util.StringTokenizer;

public class TaskData
{
    public static ItemsContainer getItemsContainer()
    {
        return itemsContainer;
    }

    public static double getMaxWeight()
    {
        return maxWeight;
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

            while (itemTokenizer.hasMoreTokens())
            {
                StringTokenizer parameters = new StringTokenizer(itemTokenizer.nextToken(), " ");
                int classId = Integer.parseInt(parameters.nextToken());
                double cost = Double.parseDouble(parameters.nextToken());
                double weight = Double.parseDouble(parameters.nextToken());
                builder.addItem(new Item(classId, weight, cost));
            }

            itemsContainer = builder.build();
        }
        catch (Exception ex)
        {
            throw new RuntimeException(ex);
        }
    }

    private static ItemsContainer itemsContainer;
    private static double maxWeight;

    private TaskData() {}
}
