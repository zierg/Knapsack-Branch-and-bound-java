package knapsack;

import knapsack.task.Solution;
import knapsack.task.TaskData;

public class Main
{

    public static void main(String[] args)
    {
        TaskData.load("task.ini");

        long startTime = System.currentTimeMillis();
        while (TaskData.doSubtaskExist())
        {
            TaskData.getBestSubtask().execute();
        }
        long finishTime = System.currentTimeMillis();

        System.out.println(String.format("Spent time: %s ms", finishTime - startTime));

        Solution bestSolution = TaskData.getBestSolution();

        if (bestSolution == null)
        {
            System.out.println("The best solution hasn't been found.");
            return;
        }

        System.out.println(String.format("Max weight = %s", TaskData.getMaxWeight()));
        System.out.println("The best solution has been found.");
        System.out.println(String.format("Cost: %s", bestSolution.getCost()));

        bestSolution.getItems().stream()
                .sorted((item1, item2) -> Integer.compare(item1.getClassId(), item2.getClassId()))
                .forEachOrdered(
                        (item -> System.out.println(String.format("Item %s: class = %s, cost = %s, weight = %s"
                                , item.getId()
                                , item.getClassId()
                                , item.getCost()
                                , item.getWeight()
                        )))
                );
    }
}
