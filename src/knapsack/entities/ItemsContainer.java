package knapsack.entities;

import knapsack.task.TaskData;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ItemsContainer
{
    public Item getBestItem(Collection<Integer> forbiddenClasses, Collection<Item> forbiddenItems, double weightLimit)
    {
        return items.stream().filter(
                (item) ->
                        !forbiddenClasses.contains(item.getClassId())
                                && !forbiddenItems.contains(item)
                                && item.getWeight() <= weightLimit
        ).findFirst().orElse(null);
    }

    public Collection<Item> getAllowedItems(Collection<Integer> forbiddenClasses)
    {
        return items.stream().filter((item) -> !forbiddenClasses.contains(item.getClassId())).collect(Collectors.toSet());
    }

    public Collection<Item> getItemsOfClass(int classId)
    {
        return items.stream().filter((item -> item.getClassId() == classId)).collect(Collectors.toSet());
    }

    public static ItemsContainerBuilder builder()
    {
        return new ItemsContainerBuilder();
    }

    public static class ItemsContainerBuilder
    {
        public ItemsContainerBuilder addItem(Item item)
        {
            itemsContainer.items.add(item);
            return this;
        }

        // TODO: refactor this terrible method
        public ItemsContainer build(double minCostToWeight, double maxCostToWeight, double minWeightAndCapacity, double maxWeightAndCapacity, double averageCost)
        {
            Collections.sort(itemsContainer.items, (item1, item2) ->
                             {
                                 double costSummand1 = normalizeDouble(item1.getCostToWeight(), minCostToWeight, maxCostToWeight);
                                 double weightSummand1 = normalizeDouble(item1.getWeightAndCapacity(), minWeightAndCapacity, maxWeightAndCapacity);
                                 double costSummand2 = normalizeDouble(item2.getCostToWeight(), minCostToWeight, maxCostToWeight);
                                 double weightSummand2 = normalizeDouble(item2.getWeightAndCapacity(), minWeightAndCapacity, maxWeightAndCapacity);

                                 return Double.compare(costSummand2 + weightSummand2 + item2.getCost() / averageCost,
                                                       costSummand1 + weightSummand1 + item1.getCost() / averageCost);
                             }
            );
            System.out.println(itemsContainer.items);
            return itemsContainer;
        }

        private final ItemsContainer itemsContainer = new ItemsContainer();
    }

    private static double normalizeDouble(double val, double min, double max)
    {
        return (val - min) / (max - min);
    }


    private ItemsContainer() {}

    private final List<Item> items = new ArrayList<>();
}
