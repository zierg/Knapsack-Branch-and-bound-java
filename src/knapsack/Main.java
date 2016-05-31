package knapsack;

import knapsack.task.Solution;
import knapsack.task.TaskData;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Main
{

    private static final boolean GENERATE_FILE = false;

    public static void main(String[] args) throws Exception
    {
        if (GENERATE_FILE)
        {
            generateRandomFile(20, 20, 150);
            exit();
        }
        TaskData.load("task.ini");

        long startTime = System.currentTimeMillis();
        while (TaskData.doSubtaskExist())
        {
            TaskData.getBestSubtask().execute();
        }
        long finishTime = System.currentTimeMillis();

        System.out.println(String.format("Spent time: %s ms", finishTime - startTime));

        Solution bestSolution = TaskData.getBestSolution();

        System.out.println("Good solutions = " + TaskData.getGoodSolutionsAmount());
        System.out.println("Bad solutions = " + TaskData.getBadSolutionsAmount());

        if (bestSolution == null)
        {
            System.out.println("The best solution hasn't been found.");
            return;
        }

        System.out.println(String.format("Max weight = %s", TaskData.getMaxWeight()));
        System.out.println("The best solution has been found.");
        System.out.println(String.format("Cost: %s", bestSolution.getCost()));

        final double [] solutionWeight = {0};

        bestSolution.getItems().stream()
                .sorted((item1, item2) -> Integer.compare(item1.getClassId(), item2.getClassId()))
                .forEachOrdered(
                        (item ->
                        {
                            System.out.println(String.format("Item %s: class = %s, cost = %s, weight = %s"
                                    , item.getId()
                                    , item.getClassId()
                                    , item.getCost()
                                    , item.getWeight()
                            ));
                            solutionWeight[0] += item.getWeight();
                        }
                        )
                );

        System.out.println("Solution weight = " + solutionWeight[0]);
    }

    private static void generateRandomFile(int classesAmount, int itemsInClassAmount, double maxWeight) throws IOException
    {
        final double COST_MAX = 30;
        final double WEIGHT_MAX = maxWeight / classesAmount * 5;
        StringBuilder b = new StringBuilder("CLASS_AMOUNT=").append(classesAmount).append("\nMAX_WEIGHT=").append(maxWeight).append("\nITEMS=");
        for (int classId = 0; classId < classesAmount; classId++)
        {
            for (int j = 0; j < itemsInClassAmount; j++)
            {
                double cost = 0.1 + Math.random() * COST_MAX;
                double weight = 0.1 + Math.random() * WEIGHT_MAX;
                b.append(classId).append(" ").append(cost).append(" ").append(weight).append(";");
            }
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
