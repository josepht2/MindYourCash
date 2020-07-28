package nu.mad.mindyourcash.models;

public class Purchase {

    public String purchaseName;
    public double cost;

    public Purchase() {
        // Default constructor required for calls to DataSnapshot.getValue(Purchase.class)
    }

    public Purchase(String purchaseName, double cost) {
        this.purchaseName = purchaseName;
        this.cost = cost;
    }
}
