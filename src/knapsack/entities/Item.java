package knapsack.entities;

public class Item
{
    public Item(int classId, double weight, double cost)
    {
        this.id = idCounter++;
        this.classId = classId;
        this.weight = weight;
        this.cost = cost;
        //this.costToWeight = cost / weight;
    }

    public int getId()
    {
        return id;
    }

    public int getClassId()
    {
        return classId;
    }

    public double getWeight()
    {
        return weight;
    }

    public double getCost()
    {
        return cost;
    }

    public double getGoodness()
    {
        return goodness;
    }

    @Override
    public String toString()
    {
        return String.format("Item %s (cl=%s, co=%s, w=%s)", id, classId, cost, weight);
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
    private final int classId;
    private final double weight;
    private final double cost;
    private double goodness;

    private static int idCounter = 1;
}
