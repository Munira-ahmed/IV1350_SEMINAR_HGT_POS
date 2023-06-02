package se.kth.iv1350.pos.view;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class TotalRevenueFileOutput extends RevenueDisplay{
    private PrintWriter logFile;

    public TotalRevenueFileOutput(String filename) throws IOException { 
        logFile = new PrintWriter(new FileWriter(filename), true);
    }

    /**
     * Show total revenue in a file.
    */
    protected void writeRevenue(){
        logFile.println("Total revenue = " + Double.toString(totalRevenue));
    }
} 