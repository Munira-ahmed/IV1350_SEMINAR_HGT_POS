package se.kth.iv1350.pos.view;


/**
* An abstract class to show total revenue.
*
*/
public abstract class RevenueDisplay implements RevenueObserver{
    protected double totalRevenue = 0;

    public void completedSale(double salePrice) {
        totalRevenue += salePrice;
        writeRevenue();
    }

    protected abstract void writeRevenue();
}
