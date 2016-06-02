package knapsack.entities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static knapsack.Logger.log;

public class ItemsContainer
{
    public Item getBestItem(Collection<Item> forbiddenItems, Collection<Item> pickedItems, double weightLimit)
    {
        return items.stream().filter(
                (item) ->
                        !forbiddenItems.contains(item) && !pickedItems.contains(item) && item.getWeight() <= weightLimit
        ).findFirst().orElse(null);
    }

    public int getItemsAmount()
    {
        return itemsAmount;
    }

    public Collection<Item> getAllowedItems(Collection<Item> forbiddenItems, Collection<Item> pickedItems, double weightLimit)
    {
        return items.stream().
                filter((item) -> !forbiddenItems.contains(item) && !pickedItems.contains(item) && item.getWeight() <= weightLimit)
                .collect(Collectors.toList());
    }

    public static ItemsContainerBuilder builder()
    {
        return new ItemsContainerBuilder();
    }

    public Collection<Item> getBestItems(Set<Item> forbiddenItems, Collection<Item> itemsInKnapsack)
    {
        return items.stream().
                filter((item -> !forbiddenItems.contains(item) && !itemsInKnapsack.contains(item)))
                .collect(Collectors.toList());
    }

    public static class ItemsContainerBuilder
    {
        public ItemsContainerBuilder addItem(Item item)
        {
            itemsContainer.items.add(item);
            return this;
        }


        public ItemsContainer build()
        {
            Collections.sort(itemsContainer.items, (item1, item2) -> Double.compare(item2.getCostToWeight(), item1.getCostToWeight()));
            log("Sorted by costtow = %s", itemsContainer.items);
            itemsContainer.itemsAmount = itemsContainer.items.size();
            System.out.println("Real item amount = " + itemsContainer.itemsAmount);
            return itemsContainer;
        }

        private final ItemsContainer itemsContainer = new ItemsContainer();

    }
    private static double normalizeDouble(double val, double min, double max) {
        return (val - min) / (max - min);
    }


    private ItemsContainer() {}

    private int itemsAmount;
    private final List<Item> items = new ArrayList<>();
}
