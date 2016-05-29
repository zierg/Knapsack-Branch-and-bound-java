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

    private final int id;
    private final int classId;
    private final double weight;
    private final double cost;
    private final double costToWeight;

    private static int idCounter = 1;
}
