package knapsack.entities;

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

        public ItemsContainer build(double averageCost, double averageWeight)
        {
            Collections.sort(itemsContainer.items, (item1, item2) ->
                    Double.compare(
                            item2.getCost() / averageCost + averageWeight / item2.getWeight()
                            , item1.getCost() / averageCost + averageWeight / item1.getWeight()
                    )
                             /*{
                                 int comparison = Double.compare(item2.getGoodness(), item1.getGoodness());
                                 if (comparison == 0)
                                 {
                                     comparison = Double.compare(item2.getCost(), item1.getCost());
                                 }
                                 return comparison;
                             }*/
            );
            System.out.println(itemsContainer.items);
            return itemsContainer;
        }

        private final ItemsContainer itemsContainer = new ItemsContainer();
    }

    private ItemsContainer() {}

    private final List<Item> items = new ArrayList<>();
}
