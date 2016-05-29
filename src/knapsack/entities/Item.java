package knapsack.entities;

public class Item
{
    public Item(int classId, double weight, double cost)
    {
        this.id = idCounter++;
        this.classId = classId;
        this.weight = weight;
        this.cost = cost;
        this.costToWeight = cost / weight;
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

    public double getCostToWeight()
    {
        return costToWeight;
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
    private final double costToWeight;

    private static int idCounter = 1;
}
