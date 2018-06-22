//package logicpuzzle;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

/**
 * PuzzleController holds all the UI and puzzle logic
 * @author Ana & Daniel
 */
public class PuzzleController extends JPanel implements ActionListener {
    private JButton leftBoxButtons[][];
    private JButton rightBoxButtons[][];
    private JButton bottomBoxButtons[][];
    private JButton hint;
    private JButton clear;
    private JButton restart;
    private PuzzleData data;
    private JTextArea clues;
    private JTextArea backstory;
    
    private VerticalLabel [] leftVertLabels;
    private VerticalLabel [] rightVertLabels;
    private JLabel [] leftLabels;
    private JLabel [] bottomLabels;

    private final int GAP = 4;
    
    /**
     * Makes a PuzzleController object complete with grid of buttons
     * @param window, the JFrame where the buttons should be
     * @param d, Puzzle data that contains all the data for the puzzle
     */
    public PuzzleController(JFrame window, PuzzleData d)
    {
        data = d;
        
        leftBoxButtons = new JButton[4][4];
        rightBoxButtons = new JButton[4][4];
        bottomBoxButtons = new JButton[4][4];
                
        leftVertLabels = new VerticalLabel[4];
        for(int i=0; i<4; i++)
        {
            leftVertLabels[i] = new VerticalLabel(data.getTopLeftLabel()[i]);
            leftVertLabels[i].setRotation(VerticalLabel.ROTATE_LEFT);
            leftVertLabels[i].setMinimumSize(new Dimension(90, 35));
            leftVertLabels[i].setMaximumSize(new Dimension(90, 35));
        }
        
        rightVertLabels = new VerticalLabel[4];
        for(int i=0; i<4; i++)
        {
            rightVertLabels[i] = new VerticalLabel(data.getBottomLabel()[i]);
            rightVertLabels[i].setRotation(VerticalLabel.ROTATE_LEFT);
            rightVertLabels[i].setMinimumSize(new Dimension(90, 35));
            rightVertLabels[i].setMaximumSize(new Dimension(90, 35));
        }
        
        leftLabels = new JLabel[4];
        for(int i=0; i<4; i++)
        {
            leftLabels[i] = new JLabel(data.getLeftLabel()[i], SwingConstants.RIGHT);
            leftLabels[i].setMinimumSize(new Dimension(90, 35));
            leftLabels[i].setMaximumSize(new Dimension(90, 35));
        }
        
        bottomLabels = new JLabel[4];
        for(int i=0; i<4; i++)
        {
            bottomLabels[i] = new JLabel(data.getBottomLabel()[i], SwingConstants.RIGHT);
            bottomLabels[i].setMinimumSize(new Dimension(90, 35));
            bottomLabels[i].setMaximumSize(new Dimension(90, 35));
        }
        
        createButtons(leftBoxButtons);
        createButtons(rightBoxButtons);
        createButtons(bottomBoxButtons);
        
        hint = new JButton("Hint");
        hint.addActionListener(this);
        
        clear = new JButton("Clear Errors");
        clear.addActionListener(this);
        
        restart = new JButton("Restart");
        restart.addActionListener(this);

        String allClues = "";
            
        for(int i=0; i<data.getClues().length; i++)
        {
            if( i == 0)
                allClues = data.getClues()[i] +"\n";
            else
                allClues += i + ". " + data.getClues()[i] + "\n";
        }
        
        clues = new JTextArea(10, 40);
        clues.setText(allClues);
        clues.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        clues.setEditable(false);
        
        backstory = new JTextArea(10, 40);
        backstory.setText(data.getBackstory());
        backstory.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        backstory.setEditable(false);
        
        GroupLayout layout = new GroupLayout(window.getContentPane());
        window.getContentPane().setLayout(layout);
        layout.setAutoCreateGaps(false);
        layout.setAutoCreateContainerGaps(true);
                
        GroupLayout.SequentialGroup sq = layout.createSequentialGroup();        
        sq.addGroup(layout.createParallelGroup()
                    .addGroup(layout.createSequentialGroup().addGroup(
                                layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                    .addGroup(horizontalGroupOfLeftButtons(layout)).addGap(GAP)
                                    .addGroup(horizontalGroupOfBottomButtons(layout)).addGap(GAP)).addGap(GAP)
                                .addGroup(horizontalGroupOfRightButtons(layout)).addGap(GAP)
                                .addComponent(clues).addGap(GAP)
                                .addComponent(backstory))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(hint).addGap(GAP)
                        .addComponent(clear).addGap(GAP)
                        .addComponent(restart).addGap(GAP)));

        layout.setHorizontalGroup(sq);
        
        GroupLayout.ParallelGroup pg = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
        pg.addGroup(layout.createSequentialGroup().addGroup(
                        layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(
                            layout.createSequentialGroup()
                                .addGroup(verticalGroupOfLeftButtons(layout)).addGap(GAP)
                                .addGroup(verticalGroupOfBottomButtons(layout)).addGap(GAP)).addGap(GAP)
                            .addGroup(verticalGroupOfRightButtons(layout)).addGap(GAP)
                            .addComponent(clues).addGap(GAP)
                            .addComponent(backstory))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addComponent(hint)
                            .addComponent(clear)
                            .addComponent(restart)));
        
        layout.setVerticalGroup(pg);
    }

    /**
     * This is a event handler that gets called by the Operating System 
     * when a click happens on a button in the GUI
     * @param e 
     */
    @Override
    public void actionPerformed(ActionEvent e)
    {
        JButton src = (JButton)e.getSource();
        //hint button clicked
        if(src == hint)
        {
            data.clearErrors(leftBoxButtons, rightBoxButtons, bottomBoxButtons);
        
            PuzzleData.Hint h = null;
            int x = 0;
            int y = 0;
            
            int i = 0;
            for(i=0; i<data.getHintSize(); i++)
            {
                boolean isCellEmpty = false;
                
                h = data.getHint(i);
                x = h.getX();
                y = h.getY();
                
                if(h.getBox().equals("l"))
                    isCellEmpty = ( leftBoxButtons[y][x].getText().length() == 0 );
                else if(h.getBox().equals("r"))
                    isCellEmpty = ( rightBoxButtons[y][x].getText().length() == 0 );
                else if(h.getBox().equals("b"))
                    isCellEmpty = ( bottomBoxButtons[y][x].getText().length() == 0 );
                
                if(isCellEmpty)
                    break;
            }
            
            if( i == data.getHintSize())
            {
                JOptionPane.showMessageDialog(this, "There are no hints left");
                return;                
            }
            
            switch(h.getBox())
            {
                case "l":
                    markFalseRowAndColumnButCellTrue(leftBoxButtons, 
                                                     y, x, 
                                                     data.getLeftBoxUserInput());
                    break;
                case "r":
                    markFalseRowAndColumnButCellTrue(rightBoxButtons, 
                                                     y, x, 
                                                     data.getRightBoxUserInput());
                    break;
                case "b":
                    markFalseRowAndColumnButCellTrue(bottomBoxButtons, 
                                                     y, x, 
                                                     data.getBottomBoxUserInput());
                    break;                    
            }
            
           JOptionPane.showMessageDialog(this, h.getMessage());
           return;
        }
        //clear button clicked
        if(src == clear)
        {
            int errors = data.clearErrors(leftBoxButtons, rightBoxButtons, bottomBoxButtons);
           
            if(errors != 0)
                JOptionPane.showMessageDialog(this,
                                    "There was "+errors+" error(s).");
            return;
        }
        
        //restart button clicked
        if(src == restart)
        {
            for(int i=0; i<4; i++)
            {
                for(int j=0; j<4; j++)
                {
                    leftBoxButtons[i][j].setText(""); //clears all the buttons in left box
                    rightBoxButtons[i][j].setText(""); //clears all the buttons in right box
                    bottomBoxButtons[i][j].setText(""); //clears all the buttons in bottom box
                    
                    data.setLeftBoxUserInput(i, j, 0);
                    data.setRightBoxUserInput(i, j, 0);
                    data.setBottomBoxUserInput(i, j, 0);
                }
            }
        }
        
        updateButtons(src, leftBoxButtons, data.getLeftBoxUserInput());
        updateButtons(src, rightBoxButtons, data.getRightBoxUserInput());
        updateButtons(src, bottomBoxButtons, data.getBottomBoxUserInput());

        if(data.checkSolution())
        {
           JOptionPane.showMessageDialog(this, "Congratulations, You WON!!!");
        }
    }

    /**
     * Private method used to update the board's buttons.
     * @param src source of click 
     * @param boxButtons buttons list
     * @param userInput  user input list
     */
    private void updateButtons(JButton src, JButton[][] boxButtons, int[][] userInput) //I added userInput 
    {
        for(int i=0; i<4; i++)
        {
            for(int j=0; j<4; j++)
            {
                if(boxButtons[i][j] == src)
                {
                    if(boxButtons[i][j].getText().length() == 0)
                    {
                        boxButtons[i][j].setText("X");
                        userInput[i][j]=-1;    
                    }else if(boxButtons[i][j].getText() == "X")//all else if is for second click
                    {
                        markFalseRowAndColumnButCellTrue(boxButtons, i, j, userInput);
                    }
                    else
                    {
                        boxButtons[i][j].setText("");
                        userInput[i][j]=0;     
                        
                        //Clear the other buttons if no other true values
                        //are present on their column or row
                        
                        //Clear the row buttons if there are no other true
                        //values on their column
                        for(int k=0; k<4; k++)
                        {
                            if(k==j) continue;
                            boolean okToClear = true;
                            
                            for(int n=0; n<4; n++)
                            {
                                if(n==i) continue;
                                if(boxButtons[n][k].getText() == "O")
                                {
                                    okToClear = false;
                                    break;
                                }
                            }
                            
                            if(okToClear)
                            {
                                boxButtons[i][k].setText("");
                                userInput[i][k]=0;  
                            }
                        }
                        
                        //Clear the column buttons if there are no other true
                        //values on their row
                        for(int k=0; k<4; k++)
                        {
                            if(k==i) continue;
                            boolean okToClear = true;
                            
                            for(int n=0; n<4; n++)
                            {
                                if(n==j) continue;
                                if(boxButtons[k][n].getText() == "O")
                                {
                                    okToClear = false;
                                    break;
                                }
                            }
                            
                            if(okToClear)
                            {
                                boxButtons[k][j].setText("");
                                userInput[k][j]=0;      
                            }
                        }                        
                    }
                }
            }
        }        
    }
    
    /**
     * 1. First click on a empty button it is marked with an "X"
     * 2. Second click on a button if in that button's row and column 
     * are no "O" present then this method will mark the clicked button 
     * with an "O" and the rest on the buttons in the row and column with an "X"
     * 3. Third click on the same button will clear the button and 
     * all the buttons in the row and column of the clicked button
     * @param boxButtons, the box of Buttons that contain the clicked button
     * @param i, the row of the clicked button
     * @param j, the column of the clicked button
     * @param userInput, user data to update
     */
    private void markFalseRowAndColumnButCellTrue(JButton[][] boxButtons, int i, int j, int[][] userInput)
    {
        //Check to see if the column already has a true value
        for(int k=0; k<4; k++)
        {
            if(k==i) continue;
            if(boxButtons[k][j].getText() == "O")
            {
                JOptionPane.showMessageDialog(this,
                    "Only one value of true is allowed "+
                    "per column.\n Remove it and try again");
                return;
            }
        }

        //Check to see if the row already has a true value
        for(int k=0; k<4; k++)
        {
            if(k==j) continue;
            if(boxButtons[i][k].getText() == "O")
            {
                JOptionPane.showMessageDialog(this,
                    "Only one value of true is allowed "+
                    "per row.\n Remove it and try again");
                return;
            }
        }                        

        //There are no TRUE values in the column or row 
        //of this clicked button
        boxButtons[i][j].setText("O");
        userInput[i][j]=1;    

        for(int k=0; k<4; k++)
        {
            if(k==i) continue;
            boxButtons[k][j].setText("X");
            userInput[k][j]=-1;     
        }

        for(int k=0; k<4; k++)
        {
            if(k==j) continue;
            boxButtons[i][k].setText("X");
            userInput[i][k]=-1;    
        }
    }
    
    @Override 
    public boolean equals(Object obj){
        if(obj == this) return true;
        if(obj == null) return false;
        if(this.getClass() == obj.getClass()){
            PuzzleController pc = (PuzzleController) obj;
            return this.data.getBackstory() == pc.data.getBackstory() && 
                    this.data.getBottomBoxUserInput() == pc.data.getBottomBoxUserInput() &&
                    this.data.getLeftBoxUserInput() == pc.data.getLeftBoxUserInput();
        }
        else{ 
            return false;
        }
    }    
    
    //Helper method to create 3 groups of buttons 
    //aranged in a 2D array
    private void createButtons(JButton[][] boxButtons) {
        for(int i=0; i<4; i++)
        {
            for(int j=0; j<4; j++)
            {
                boxButtons[i][j] = new JButton();
                boxButtons[i][j].addActionListener(this);
                boxButtons[i][j].setMargin(new Insets(1,1,1,1));
                boxButtons[i][j].setMinimumSize(new Dimension(35, 35));
                boxButtons[i][j].setMaximumSize(new Dimension(35, 35));
            }
        }
    }

    //This helper method is used to create a vertical layout group
    //for the left set of buttons
    //Used during layout setup
    private GroupLayout.Group verticalGroupOfLeftButtons(GroupLayout layout) {
        GroupLayout.SequentialGroup retVal = layout.createSequentialGroup();
        GroupLayout.ParallelGroup col = layout.createParallelGroup(GroupLayout.Alignment.TRAILING);

        for(int i=0; i<4; i++)
        {
            col.addComponent(leftVertLabels[i]);
        }
        retVal.addGroup(col).addGap(GAP);
        
        for(int i=0; i<4; i++)
        {
            //For each row of buttons create a sequential group
            col = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
            col.addComponent(leftLabels[i]).addGap(GAP);
            for(int j=0; j<4; j++)
            {
                col.addComponent(leftBoxButtons[i][j]);
            }
            retVal.addGroup(col);
        }
        
        return retVal;
    }

    //This helper method is used to create a horizontal layout group
    //for the left set of buttons
    //Used during layout setup
    private GroupLayout.Group horizontalGroupOfLeftButtons(GroupLayout layout) {
        GroupLayout.ParallelGroup retVal = layout.createParallelGroup(GroupLayout.Alignment.TRAILING);
        GroupLayout.SequentialGroup row = layout.createSequentialGroup();
        
        for(int i=0; i<4; i++)
        {
            row.addComponent(leftVertLabels[i]);
        }
        retVal.addGroup(row).addGap(GAP);
        
        for(int i=0; i<4; i++)
        {
            row = layout.createSequentialGroup();
            row.addComponent(leftLabels[i]).addGap(GAP);
            for(int j=0; j<4; j++)
            {
                row.addComponent(leftBoxButtons[i][j]);
            }
            retVal.addGroup(row);
        }
        
        return retVal;
    }

    //This helper method is used to create a horizontal layout group
    //for the set of buttons on the right
    //Used during layout setup    
    private GroupLayout.Group horizontalGroupOfRightButtons(GroupLayout layout) {
        GroupLayout.ParallelGroup retVal = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
        GroupLayout.SequentialGroup row = layout.createSequentialGroup();
        
        for(int i=0; i<4; i++)
        {
            row.addComponent(rightVertLabels[i]);
        }
        retVal.addGroup(row).addGap(GAP);
        
        for(int i=0; i<4; i++)
        {
            row = layout.createSequentialGroup();
            for(int j=0; j<4; j++)
            {
                row.addComponent(rightBoxButtons[i][j]);
            }
            retVal.addGroup(row);
        }
        
        return retVal;
    }

    //This helper method is used to create a vertical layout group
    //for the set of buttons on the right
    //Used during layout setup        
    private GroupLayout.Group verticalGroupOfRightButtons(GroupLayout layout) {
        GroupLayout.SequentialGroup retVal = layout.createSequentialGroup();
        GroupLayout.ParallelGroup col = layout.createParallelGroup(GroupLayout.Alignment.LEADING);

        for(int i=0; i<4; i++)
        {
            col.addComponent(rightVertLabels[i]);
        }
        retVal.addGroup(col).addGap(GAP);
        
        for(int i=0; i<4; i++)
        {
            //For each row of buttons create a sequential group
            col = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
            for(int j=0; j<4; j++)
            {
                col.addComponent(rightBoxButtons[i][j]);
            }
            retVal.addGroup(col);
        }
        
        return retVal;
    }

    //This helper method is used to create a vertical layout group
    //for the set of buttons on the bottom
    //Used during layout setup    
    private GroupLayout.Group verticalGroupOfBottomButtons(GroupLayout layout) {
        GroupLayout.ParallelGroup retVal = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
        GroupLayout.SequentialGroup row = layout.createSequentialGroup();
        
        for(int i=0; i<4; i++)
        {
            row.addComponent(bottomLabels[i]);
        }
        retVal.addGroup(row).addGap(GAP);
        
        for(int i=0; i<4; i++)
        {
            row = layout.createSequentialGroup();
            for(int j=0; j<4; j++)
            {
                row.addComponent(bottomBoxButtons[i][j]);
            }
            retVal.addGroup(row);
        }
        
        return retVal;
    }

    //This helper method is used to create a horizontal layout group
    //for the set of buttons on the bottom
    //Used during layout setup
    private GroupLayout.Group horizontalGroupOfBottomButtons(GroupLayout layout) {
        GroupLayout.SequentialGroup retVal = layout.createSequentialGroup();
        GroupLayout.ParallelGroup col = layout.createParallelGroup(GroupLayout.Alignment.LEADING);

        for(int i=0; i<4; i++)
        {
            col.addComponent(bottomLabels[i]);
        }
        retVal.addGroup(col).addGap(GAP);
        
        for(int i=0; i<4; i++)
        {
            //For each row of buttons create a sequential group
            col = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
            for(int j=0; j<4; j++)
            {
                col.addComponent(bottomBoxButtons[i][j]);
            }
            retVal.addGroup(col);
        }
        
        return retVal;        
    }
}