/*
 * TCSS 305 - SnapShop GUI
 */

package gui;

import filters.EdgeDetectFilter;
import filters.EdgeHighlightFilter;
import filters.Filter;
import filters.FlipHorizontalFilter;
import filters.FlipVerticalFilter;
import filters.GrayscaleFilter;
import filters.SharpenFilter;
import filters.SoftenFilter;
import image.PixelImage;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;


/**
 * The graphical user interface for the SnapShop program.
 * @author Jasmine Dacones
 * @version 4/30/16
 */
public class SnapShopGUI extends JPanel {
    
    /** Automatically generated serial ID number. */
    private static final long serialVersionUID = 5976844051772837995L;

    /** Padding for buttons. */
    private static final int PADDING = 6;
    
    /** Length of file chooser dialog box. */
    private static final int FILECHOOSERSIZEL = 600;
    
    /** Width of file chooser dialog box. */
    private static final int FILECHOOSERSIZEW = 400;
  
    /** SnapShop GUI window. */
    private final JFrame myFrame; 
    
    /** Menu Panel. */
    private final JPanel myMenuPanel;
    
    /** Top set of buttons on menu panel. */
    private final JPanel myTopPanel;
    
    /** Bottom set of buttons on menu panel. */
    private final JPanel myBottomPanel;
    
    /** Image holder panel. */
    private final JPanel myImagePanel;
    
    /** Save button. */
    private JButton mySave;
    
    /** Close button. */
    private JButton myClose;
    
    /** JFileChooser for the open file dialog. */
    private final JFileChooser myFileChooser;
    
    /** Image file. */
    private PixelImage myImage;
    
    /** JLabel to hold image. */
    private final JLabel myLabel;
    
    /** ArrayList of Filter Buttons. */
    private final List<JButton> myFilterButtons;
    
   
    /**
     * Initializes all fields.
     */
    public SnapShopGUI() {
        myFrame = new JFrame("TCSS 305 SnapShop");
        myFrame.getContentPane().setLayout(new BorderLayout());
    
        myMenuPanel = new JPanel(new BorderLayout());     
        myTopPanel = new JPanel(new GridLayout(0, 1, PADDING, PADDING));              
        myBottomPanel = new JPanel(new GridLayout(0, 1, PADDING, PADDING));                   
        myImagePanel = new JPanel(new BorderLayout());
                    
        myLabel = new JLabel();

        myFileChooser = new JFileChooser();       
        myFileChooser.setPreferredSize(new Dimension(FILECHOOSERSIZEL, FILECHOOSERSIZEW));
        
        myFilterButtons = new ArrayList<JButton>();                      
    }
    
    /**
     * Creates the SnapShop GUI program.
     */
    public void start() {
        final int border = 10;
        final int smallerBorder = 3;

        myTopPanel.setBorder(new EmptyBorder(border, border, smallerBorder, border));
        myBottomPanel.setBorder(new EmptyBorder(smallerBorder, border, border, border));  
              
        myImagePanel.setBackground(Color.WHITE);
        
        myImagePanel.add(myLabel, BorderLayout.NORTH);
        
        createFilters();
        
        myBottomPanel.add(createOpenButton());
        myBottomPanel.add(createSaveButton());
        myBottomPanel.add(createCloseButton());
        
        myMenuPanel.add(myTopPanel, BorderLayout.NORTH);
        myMenuPanel.add(myBottomPanel, BorderLayout.SOUTH);     
        
        myFrame.add(myMenuPanel, BorderLayout.WEST);
        myFrame.add(myImagePanel);
        
        myFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);              
        myFrame.pack();     
        myFrame.setVisible(true);
        myFrame.setLocationRelativeTo(null); // put this last to center window
       
    }
    
    /**
     * Creates all the filters and adds it to
     * an ArrayList.
     */
    private void createFilters() {
        
        final ArrayList<Filter> filters = new ArrayList<Filter>();
        filters.add(new EdgeDetectFilter());
        filters.add(new EdgeHighlightFilter());
        filters.add(new FlipHorizontalFilter());
        filters.add(new FlipVerticalFilter());
        filters.add(new GrayscaleFilter());
        filters.add(new SharpenFilter());
        filters.add(new SoftenFilter());
        
        for (final Filter filter : filters) {
            myTopPanel.add(createFilterButton(filter));         
        }
    }
    
    /**
     * Creates a filter button and its action.
     * @param theFilter a filter
     * @return a filter button.
     */
    private JButton createFilterButton(final Filter theFilter) {
        final JButton fButton = new JButton(theFilter.getDescription());

        fButton.setEnabled(false);
        myFilterButtons.add(fButton);
        
        /**
         * Inner class ActionListener for the Filter button.
         * @author Jasmine Dacones
         * @version 5/1/16
         */
        class FilterActionListener implements ActionListener {

            @Override
            public void actionPerformed(final ActionEvent theEvent) {
                theFilter.filter(myImage);
                myLabel.setIcon(new ImageIcon(myImage));               
            }           
        }
        
        fButton.addActionListener(new FilterActionListener());        
        return fButton;
    }
    
    /**
     * Creates the open button and its action.
     * @return the open button.
     */
    private JButton createOpenButton() {
        final JButton open = new JButton("Open...");
        
        /**
         * Inner class ActionListener for the Open button.
         * @author Jasmine Dacones
         * @version 5/1/16
         */
        class OpenActionListener implements ActionListener {

            @Override
            public void actionPerformed(final ActionEvent theEvent) {
                myFileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
                final int returnVal = myFileChooser.showOpenDialog(myFrame);

                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    final File file = myFileChooser.getSelectedFile();
                    try {
                        myImage = PixelImage.load(file);
                        myLabel.setIcon(new ImageIcon(myImage));
                        myFrame.pack();
                    } catch (final IOException fileErr) {
                        JOptionPane.showMessageDialog(myFrame,
                                "The selected file did not contain an image.",
                                "Error!",
                                JOptionPane.ERROR_MESSAGE);
                    }
                    
                    for (final JButton button : myFilterButtons) {
                        button.setEnabled(true);
                    }
                    
                    mySave.setEnabled(true);
                    myClose.setEnabled(true);
                }         
            }
        }
        
        open.addActionListener(new OpenActionListener());       
        return open;       
    }
    
    /**
     * Creates the save button and its action.
     * @return the save button.
     */
    private JButton createSaveButton() { 
        mySave = new JButton("Save As...");
        mySave.setEnabled(false);
        
        /**
         * Inner Class ActionListener for the Save As button.
         * @author Jasmine Dacones
         * @version 5/1/16
         *
         */
        class SaveActionListener implements ActionListener {

            @Override
            public void actionPerformed(final ActionEvent theEvent) {  
                final int returnVal = myFileChooser.showSaveDialog(myFrame);

                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    try {
                        myImage.save(myFileChooser.getSelectedFile());
                    } catch (final IOException e1) {
                        e1.printStackTrace();
                    }           
                }           
            }
        }
        
        mySave.addActionListener(new SaveActionListener());
        return mySave;      
    }
    
    /**
     * Creates the close button and its action.
     * @return the close button.
     */
    private JButton createCloseButton() {
        myClose = new JButton("Close");
        myClose.setEnabled(false);
        
        /**
         * Inner class ActionListener for the Close button.
         * @author Jasmine Dacones
         * @version 5/1/16
         */
        class CloseActionListener implements ActionListener {

            @Override
            public void actionPerformed(final ActionEvent theEvent) {
                myLabel.setIcon(null);  
                for (final JButton button : myFilterButtons) {
                    button.setEnabled(false);
                }
                myClose.setEnabled(false);
                mySave.setEnabled(false);
                myFrame.pack();
            }       
        }
        
        myClose.addActionListener(new CloseActionListener());     
        return myClose;
    }
}
