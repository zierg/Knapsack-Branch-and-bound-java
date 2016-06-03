package knapsack;

import knapsack.entities.Item;
import knapsack.task.Solution;
import knapsack.task.Subtask;
import knapsack.task.CommonData;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.stream.Collectors;

public class Main
{

    private static final boolean GENERATE_FILE = false;

    public static void main(String[] args) throws Exception
    {
        if (GENERATE_FILE)
        {
            generateRandomFile(100, 20);
            System.out.println("A new file has been generated.");
        }
        CommonData.load("task.ini");

        long startTime = System.currentTimeMillis();
        while (CommonData.doSubtaskExist())
        {
            Subtask best = CommonData.getBestSubtask();
            best.execute();
        }
        long finishTime = System.currentTimeMillis();

        Solution bestSolution = CommonData.getBestSolution();

        if (bestSolution == null)
        {
            System.out.println("The best solution hasn't been found.");
            return;
        }

        List<Item> sorted = bestSolution.getItems().stream().sorted((item1, item2) -> Integer.compare(item1.getRealId(), item2.getRealId())).collect(Collectors.toList());
        Item previousItem = sorted.get(0);
        int itemAmount = 0;
        int previousRealId = previousItem.getRealId();
        for (Item item : sorted)
        {

            if (item.getRealId() == previousRealId)
            {
                itemAmount++;
                continue;
            }
            printItem(previousItem, itemAmount);
            itemAmount = 1;
            previousRealId = item.getRealId();
            previousItem = item;
        }
        printItem(previousItem, itemAmount);


        System.out.println("The best solution has been found.");
        System.out.println(String.format("Max weight = %s", CommonData.getMaxWeight()));
        System.out.println("Solution weight = " + bestSolution.getWeight());
        System.out.println(String.format("Cost: %s", bestSolution.getCost()));

        System.out.println(String.format("Spent time: %s ms", finishTime - startTime));
        System.out.println("Good solutions = " + CommonData.getGoodSolutionsAmount());
        System.out.println("Bad solutions = " + CommonData.getBadSolutionsAmount());


    }

    private static void printItem(Item item, int amount)
    {
        System.out.println(String.format("Item %s (%s): cost = %s, weight = %s. Total cost = %s, weight = %s"
                , item.getRealId()
                , amount
                , item.getCost()
                , item.getWeight()
                , item.getCost() * (double) amount
                , item.getWeight() * (double) amount
        ));
    }

    private static void generateRandomFile(int itemsAmount, double maxWeight) throws IOException
    {
        DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols();
        otherSymbols.setDecimalSeparator('.');
        DecimalFormat myFormatter = new DecimalFormat("###", otherSymbols);
        final double COST_MAX = 30;
        final double WEIGHT_MAX = 50;
        StringBuilder b = new StringBuilder("MAX_WEIGHT=").append(maxWeight).append("\nITEMS=");
        for (int i = 0; i < itemsAmount; i++)
        {
            int limit = (int) (Math.random() * 10d + 1d);
            double cost = 1 + Math.random() * COST_MAX;
            double weight = 1 + Math.random() * WEIGHT_MAX;
            b.append(limit).append(" ").append(myFormatter.format(cost)).append(" ").append(myFormatter.format(weight)).append(";");
        }

        File file = new File("task.ini");
        if (!file.exists())
        {
            file.createNewFile();
        }
        try (FileWriter out = new FileWriter(file))
        {
            out.write(b.toString());
        }
    }

    private static void exit()
    {
        System.exit(0);
    }
}
