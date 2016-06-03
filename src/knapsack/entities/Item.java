package knapsack.entities;

public class Item
{
    public Item(double weight, double cost, boolean next)
    {
        this.id = idCounter++;
        if (next)
        {
            realId = realIdCounter++;
        }
        else
        {
            realId = realIdCounter;
        }
        this.weight = weight;
        this.cost = cost;
        this.costToWeight = cost / weight;
    }

    public int getId()
    {
        return id;
    }

    public int getRealId()
    {
        return realId;
    }

    public double getWeight()
    {
        return weight;
    }

    public double getCost()
    {
        return cost;
    }

    public double getCostToWeight()
    {
        return costToWeight;
    }


    @Override
    public String toString()
    {
        return String.format("Item %s (co=%s, w=%s)", id,  cost, weight);
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (o == null || getClass() != o.getClass())
        {
            return false;
        }

        Item item = (Item) o;

        return id == item.id;

    }

    @Override
    public int hashCode()
    {
        return id;
    }

    private final int id;
    private final int realId;

    private final double weight;
    private final double cost;
    private final double costToWeight;

    private static int idCounter = 1;
    private static int realIdCounter = 1;
}
