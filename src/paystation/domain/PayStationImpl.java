package paystation.domain;

import java.util.HashMap;
import java.util.Map;

/**
 * Implementation of the pay station.
 *
 * Responsibilities:
 *
 * 1) Accept payment; 
 * 2) Calculate parking time based on payment; 
 * 3) Know earning, parking time bought; 
 * 4) Issue receipts; 
 * 5) Handle buy and cancel events.
 *
 * This source code is from the book "Flexible, Reliable Software: Using
 * Patterns and Agile Development" published 2010 by CRC Press. Author: Henrik B
 * Christensen Computer Science Department Aarhus University
 *
 * This source code is provided WITHOUT ANY WARRANTY either expressed or
 * implied. You may study, use, modify, and distribute it for non-commercial
 * purposes. For any commercial use, see http://www.baerbak.com/
 */
public class PayStationImpl implements PayStation {
    
    private int insertedSoFar;
    private int timeBought;

    // ints with initialization
    private int numberOfNickels=0;
    private int numberOfDimes=0;
    private int numberOfQuarters=0;

    // create a map to use for coin types
    private Map coinMap= new HashMap<Integer, Integer>();

    @Override
    public void addPayment(int coinValue)
            throws IllegalCoinException {
        switch (coinValue) {
            case 5: {
                numberOfNickels++;
                coinMap.put(numberOfNickels, numberOfNickels);
                break;
                }
            case 10: {
                numberOfDimes++;
                coinMap.put(numberOfDimes, numberOfDimes);
                break;
                }
            case 25: {
                numberOfQuarters++;
                coinMap.put(numberOfQuarters, numberOfQuarters);
                break;
                }
            default:
                throw new IllegalCoinException("Invalid coin: " + coinValue);
        }
        insertedSoFar += coinValue;
        timeBought = insertedSoFar / 5 * 2;
    }

    @Override
    public int readDisplay() {
        return timeBought;
    }

    @Override
    public Receipt buy() {
        Receipt r = new ReceiptImpl(timeBought);
        reset();

        //reset the coinMap
        clearMap();

        return r;
    }

    public Map cancel() {

        Map tempMap = coinMap;

        //call empty, which also calls reset
        empty();
        //clear the coinMap
        clearMap();

        //@return A Map defining the coins returned to the user.
        return tempMap;
    }
    
    private void reset() {
        timeBought = insertedSoFar = 0;
    }

    private void clearMap(){
        coinMap.put(numberOfNickels, 0);
        coinMap.put(numberOfDimes, 0);
        coinMap.put(numberOfQuarters, 0);
    }

    private int empty(){

        // hold the amount inserted so far since it will be deleted momentarily
        int tempInsertedSoFar = insertedSoFar;

        // empties it, setting the total to zero. (Note that money is only collected after a call to buy.)
        numberOfNickels = 0;
        numberOfDimes = 0;
        numberOfQuarters = 0;

        // call reset
        reset();

        // return to the person the total amount of money collected by the paystation since the last call
        return tempInsertedSoFar;
    }
}
