package se.kth.iv1350.pos.view;

import se.kth.iv1350.pos.integration.ItemDTO;
import se.kth.iv1350.pos.integration.LogHandler;
import se.kth.iv1350.pos.integration.ErrorMessageHandler;

import java.io.IOException;
import java.util.Random;

import se.kth.iv1350.pos.controller.Controller;
import se.kth.iv1350.pos.controller.Controller.InventoryFailException;
import se.kth.iv1350.pos.model.SaleInformation.ItemNotFoundException;
import se.kth.iv1350.pos.integration.TotalRevenueFileOutput;

/**
* This is the program's only View class, which is used for calling all the methods by cashier in controller.
* Since there is no actural activities from view, there is a hard-code to show the whole sale rocess.
*/ 
public class View {
    private Controller controller;
    private ErrorMessageHandler errorMessageHandler;
    private LogHandler logHandler;
    

/**
* Create the View.
* @param controller The Controller should be entered.
* @param logFilename The filename of the log file.
*/
    public View(Controller controller, String logFilename, String revenueLogFile) throws IOException
    {
        this.controller = controller;
        this.controller.addSaleObserver(new TotalRevenueView());
        this.controller.addSaleObserver(new TotalRevenueFileOutput(revenueLogFile));
        this.errorMessageHandler = new ErrorMessageHandler();
        this.logHandler = new LogHandler(logFilename);
    }

/**
* Hard-coded method calls to run the sale process.
* All the methods calling pass through controller.
* Everything that is returned by the controller as well as the receipt will be printed out.
*/
    public void runFakeScenario(){
        Random ran = new Random();
        int allCustomerId[] = {1234, 5678, 9012};
        for (int customerId : allCustomerId) {

            System.out.println("\n");
            System.out.println("Customer id " + Integer.toString(customerId) + " walks into the store.");
    
            ItemDTO cola = new ItemDTO(778020, "Cola", "500ml", 0.2, 15);
            int colaId = cola.getId();
    
            ItemDTO chips = new ItemDTO(520001, "OLW chips", "250g", 0.2, 30);
            int chipsId = chips.getId();
    
            ItemDTO glass = new ItemDTO(339800, "Hagendas", "Strawberry", 0.2, 70);
            int glassId = glass.getId();
    
            ItemDTO nonExist = new ItemDTO(-1, "Heaven", "nowhere", 1, 1000);
            int nonExistId = nonExist.getId();
    
            ItemDTO failure = new ItemDTO(0, "Fail", "netConnection", 0, 0);
            int failureId = failure.getId();
    
            double paidAmount = 1000;
    
            System.out.println("The customer starts the sale.");
            controller.startSale();
    
            int colaQuantity = ran.nextInt(5);
            System.out.println("The customer buys " + Integer.toString(colaQuantity) + " Cola.");
            try {
                controller.enterItem(colaId, colaQuantity);
            } catch (ItemNotFoundException e) {
                this.errorMessageHandler.showErrorMsg("The item " + Integer.toString(colaId) + " does not exist");
                this.logHandler.log("The item " + Integer.toString(colaId) + " does not exist.");
            } catch(InventoryFailException e){
                this.errorMessageHandler.showErrorMsg("Fail to reach the database for item: " + Integer.toString(colaId));
                this.logHandler.log("Fail to reach the databasefor item: " + Integer.toString(colaId));
            }
    
            int chipsQuantity = ran.nextInt(5);
            System.out.println("The customer buys " + Integer.toString(chipsQuantity) + " Chips.");
            try {
                controller.enterItem(chipsId, chipsQuantity);
            } catch (ItemNotFoundException e) {
                this.errorMessageHandler.showErrorMsg("The item " + Integer.toString(chipsId) + " does not exist.");
                this.logHandler.log("The item " + Integer.toString(chipsId) + " does not exist.");
            } catch(InventoryFailException e){
                this.errorMessageHandler.showErrorMsg("Fail to reach the database for item: " + Integer.toString(chipsId));
                this.logHandler.log("Fail to reach the database for item: " + Integer.toString(chipsId));
            }
    
            int glassQuantity = ran.nextInt(5);
            System.out.println("The customer buys " + Integer.toString(glassQuantity) + " glass.");
            try {
                controller.enterItem(glassId, glassQuantity);
            } catch (ItemNotFoundException e) {
                this.errorMessageHandler.showErrorMsg("The item " + Integer.toString(glassId) + " does not exist.");
                this.logHandler.log("The item " + Integer.toString(glassId) + " does not exist.");
            } catch(InventoryFailException e){
                this.errorMessageHandler.showErrorMsg("Fail to reach the database for item: " + Integer.toString(glassId));
                this.logHandler.log("Fail to reach the database for item: " + Integer.toString(glassId));
            }
            
            System.out.println("The customer buys 1 more Hagendas.");
            try {
                controller.enterItem(glassId, 1);
            } catch (ItemNotFoundException e) {
                this.errorMessageHandler.showErrorMsg("The item " + Integer.toString(glassId) + " does not exist.");
                this.logHandler.log("The item " + Integer.toString(glassId) + " does not exist.");
            } catch(InventoryFailException e){
                this.errorMessageHandler.showErrorMsg("Fail to reach the database for item: " + Integer.toString(glassId));
                this.logHandler.log("Fail to reach the database for item: " + Integer.toString(glassId));
            }
    
            System.out.println("The customer buys 1 invalid item.");
            try {
                controller.enterItem(nonExistId, 1);
            } catch (ItemNotFoundException e) {
                this.errorMessageHandler.showErrorMsg("The item " + Integer.toString(nonExistId) + " does not exist.");
                this.logHandler.log("The item " + Integer.toString(nonExistId) + " does not exist.");
            } catch(InventoryFailException e){
                this.errorMessageHandler.showErrorMsg("Fail to reach the database.");
                this.logHandler.log("Fail to reach the database for item: " + Integer.toString(nonExistId));
            }
    
            System.out.println("Try to simulate database failed to reach for item " + Integer.toString(failureId));
            try {
                controller.enterItem(failureId, 1);
            } catch (ItemNotFoundException e) {
                this.errorMessageHandler.showErrorMsg("The item " + Integer.toString(failureId) + " does not exist.");
                this.logHandler.log("The item " + Integer.toString(failureId) + " does not exist.");
            } catch(InventoryFailException e){
                this.errorMessageHandler.showErrorMsg("Fail to reach the database.");
                this.logHandler.log("Fail to reach the database for item: " + Integer.toString(failureId));
            }
    
            
            System.out.println("The customer asks for discounts.");
            controller.sendDiscountRequest(customerId);
    
            System.out.println("The customer asks to end the sale.");
            controller.endSale();
    
            System.out.println("The customer pays and get the receipt.");
            System.out.println("\n");
            controller.printReceipt(paidAmount);

        }
    }
}
