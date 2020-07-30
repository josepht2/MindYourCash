package nu.mad.mindyourcash.models;

public class Purchase {

    public String purchaseName;
    public double cost;
    public String category;
    public String date;

    public Purchase() {
        // Default constructor required for calls to DataSnapshot.getValue(Purchase.class)
    }

    public Purchase(String purchaseName, double cost, String category, String date) {
        this.purchaseName = purchaseName;
        this.cost = cost;
        this.category = category;
        this.date = date;
    }
}
