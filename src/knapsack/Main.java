package knapsack;

import knapsack.task.Solution;
import knapsack.task.TaskData;

public class Main
{

    public static void main(String[] args)
    {
        TaskData.load("task.ini");

        while (TaskData.doSubtaskExist())
        {
            TaskData.getBestSubtask().execute();
        }

        Solution bestSolution = TaskData.getBestSolution();

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
