package knapsack.entities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class ItemsContainer
{
    public Item getBestItem(Collection<Integer> forbiddenClasses, Collection<Integer> forbiddenItems)
    {
        return items.stream().filter(
                (item) -> !forbiddenClasses.contains(item.getClassId()) && !forbiddenItems.contains(item.getId())
        ).findFirst().get();
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

        public ItemsContainer build()
        {
            Collections.sort(itemsContainer.items, (item1, item2) -> Double.compare(item2.getCostToWeight(), item1.getCostToWeight()));
            return itemsContainer;
        }

        private final ItemsContainer itemsContainer = new ItemsContainer();
    }

    private ItemsContainer() {}

    private final List<Item> items = new ArrayList<>();
}
