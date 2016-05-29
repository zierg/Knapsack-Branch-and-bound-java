package knapsack.entities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ItemsContainer
{
    public Item getBestItem(Collection<Integer> forbiddenClasses, Collection<Item> forbiddenItems)
    {
        return items.stream().filter(
                (item) -> !forbiddenClasses.contains(item.getClassId()) && !forbiddenItems.contains(item)).
                findFirst().get();
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
