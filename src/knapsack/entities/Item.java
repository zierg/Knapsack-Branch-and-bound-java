package knapsack.entities;

import knapsack.task.TaskData;

public class Item
{
    public Item(int classId, double weight, double cost, int classAmount)
    {
        this.id = idCounter++;
        this.classId = classId;
        this.weight = weight;
        this.cost = cost;
        this.costToWeight = cost / weight;
        double maxToClasses = TaskData.getMaxWeight() / (double) classAmount;
        if (maxToClasses == weight)
        {
            maxToClasses += 0.01; // TODO: remove magic number
        }
        this.weightAndCapacity = 1d / Math.abs(maxToClasses - weight);
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

    public double getWeightAndCapacity()
    {
        return weightAndCapacity;
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
    private final double costToWeight;
    private final double weightAndCapacity;

    private static int idCounter = 1;
}
