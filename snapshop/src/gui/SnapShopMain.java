/*
 * TCSS 305 - Assignment 3: SnapShop
 */

package gui;


import java.awt.EventQueue;

/**
 * Runs SnapShop by instantiating and starting the SnapShopGUI.
 * 
 * @author Marty Stepp
 * @author Daniel M. Zimmerman
 * @author Alan Fowler
 * @version Spring 2015
 */
public final class SnapShopMain {

    /**
     * Private constructor, to prevent instantiation of this class.
     */
    private SnapShopMain() {
        throw new IllegalStateException();
    }

    
    /**
     * Creates a JFrame to demonstrate BorderLayout.
     * It is OK, even typical to include a main method 
     * in the same class file as a GUI for testing purposes. 
     * 
     * @param theArgs Command line arguments, ignored.
     */
    public static void main(final String[] theArgs) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new SnapShopGUI().start();
            }
        });
    }
}
