package se.kth.iv1350.pos.view;

/**
 * Shows a running total of a sale process of each sale.
*/

class TotalRevenueView extends RevenueDisplay{

    protected void writeRevenue(){
        System.out.println("### Current income of total sales is " + Double.toString(totalRevenue) + ". ### "); 
    }
}


