package se.kth.iv1350.pos.controller;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.ArrayList;
import se.kth.iv1350.pos.integration.ItemDTO;
import se.kth.iv1350.pos.integration.DiscountRegistry;
import se.kth.iv1350.pos.integration.ExternalInventorySystem;
import se.kth.iv1350.pos.integration.AccountingSystem;
import se.kth.iv1350.pos.integration.CustomerRegistry;
import se.kth.iv1350.pos.integration.RegisterCreator;
import se.kth.iv1350.pos.integration.ExternalInventorySystem.DatabaseFailureException;
import se.kth.iv1350.pos.model.SaleInformation;
import se.kth.iv1350.pos.integration.Printer;
import se.kth.iv1350.pos.model.Receipt;
import se.kth.iv1350.pos.view.RevenueObserver;

public class Controller {
    private SaleInformation saleInformation;
    private ExternalInventorySystem externalInventorySystem;
    private DiscountRegistry discountRegistry;
    private AccountingSystem accountingSystem;
    private Printer printer;
    private ArrayList<RevenueObserver> revenueObservers = new ArrayList<>();

/**
* This is the only Controller class in the program, calling all the other methods in model and integration.
* @param registerCreator This object contains the methods to create new externalInventorySystem, discountRegistry, customerRegistry in controller.
* @param printer The object Printer which contains the method to print receipt out will be used.
*/
    public Controller (RegisterCreator registerCreator, Printer printer){
        this.externalInventorySystem = registerCreator.getItemRegistry();
        this.discountRegistry = registerCreator.getDiscountRegistry();
        this.accountingSystem = registerCreator.getAccountingSystem();
        this.printer = printer;
    }

/**
* Get the SaleInformation in controller (using for test).
*
* @return saleInformation The object SaleInformation will be returned.
*/
    public SaleInformation getSaleInformation(){
        return saleInformation;
    }

/**
* Start a new sale with a new initialized SaleInformation.
* So that sold items can be entered and added during the following sale process.
*/
    public void startSale(){
        saleInformation = new SaleInformation();
    }

/**
* Enter the sold items to the saleInformation.
* @param identifier The int which is the soldItem ID number will be scanned in.
* @param quantity The int number which will be entered by view.
* @return saleInformation The new saleInformation uppdated with new entered items.
* @throws ItemNotFoundException when the entered item identifier can not be found in inventory.
* @throws InventoryFailException when the inventory database can not be reached.
*/
    public SaleInformation enterItem (int identifier, int quantity)
        throws SaleInformation.ItemNotFoundException, InventoryFailException{
            try {
                saleInformation.addItem(identifier, quantity, externalInventorySystem);
                saleInformation.uppdateSaleInformation();
                } 
            catch(DatabaseFailureException e) { 
                throw new InventoryFailException("Could not get the inventory for item " + Integer.toString(identifier));
                }
        
        return saleInformation;
    }

/**
* Thrown when database can not be called.
*/
public class InventoryFailException extends Exception { 
    public InventoryFailException(String errorMessage) {
        super(errorMessage);
    }
}

/**
* Apply discount request from view.
* @param customerId The int which will be entered by view.
* @return priceAfterDiscount The double of price after that requested discounts is applied.
*/ 
    public double sendDiscountRequest (int customerId){
        double priceAfterDiscount = 0;
        priceAfterDiscount = saleInformation.includeDiscount(customerId, discountRegistry);
        return priceAfterDiscount;
    }

/**
* End the sale process with inventory system and accounting system uppdated.
* @return SaleInformation The uppdated final saleInformation object will be returned.
*/ 
    public SaleInformation endSale(){
        HashMap<ItemDTO,Integer> soldItems = saleInformation.getSoldItems();
        externalInventorySystem.uppdateInventory(soldItems);
        saleInformation.uppdateSaleInformation();
        notifyObservers();
        return saleInformation;
    }

/**
 * The specified observer will be notified when a sale
 * has been ended. There will be notifications only for
 * sales that are started after this method is called.
 *
* @param revenueObserver The observer to notify.
*/
public void addSaleObserver(RevenueObserver revenueObserver) { 
    revenueObservers.add(revenueObserver);
}

private void notifyObservers() {
    for (RevenueObserver obs : revenueObservers) {
            obs.completedSale(saleInformation.getTotalPrice());
        }
}

/**
* Print the receipt out of the sale.
* @param paidAmount The double which will be entered by view.
*/ 
    public void printReceipt(double paidAmount){
        HashMap<ItemDTO,Integer> soldItems;
        Receipt receipt;
        double totalPrice = saleInformation.getTotalPrice();
        soldItems = saleInformation.getSoldItems();
        LocalDateTime dateTime = LocalDateTime.now();
        receipt = new Receipt(paidAmount, totalPrice, soldItems, dateTime);
        printer.printReceipt(receipt);
    }

}

