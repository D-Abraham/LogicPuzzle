
//package logicpuzzle;


import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.JFrame;

/**
 * PuzzleUI main class creates a PuzzleController and a PuzzleData
 * Loads PuzzleData object with the contents of the puzzle data file
 * and supplies that to the PuzzleController
 * @author Ana
 */
public class PuzzleUI extends JFrame{
    private PuzzleController puzzleButtons;
    private PuzzleData puzzleData;

    public static void main(String[] args) {
        PuzzleUI ex = new PuzzleUI();
        ex.setVisible(true);
    }
    //constructor
    public PuzzleUI()
    {
        makeController();
    }    
    //creates a PuzzleController and a PuzzleData object
    //Loads PuzzleData object with Puzzle information.
    //Assigns PuzzleData to PuzzleController
    private void makeController() {
        puzzleData = new PuzzleData();
        puzzleData.readDB("PuzzleDB");
        
        puzzleButtons = new PuzzleController(this, puzzleData);
        
        pack();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }
}