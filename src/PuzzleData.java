//package logicpuzzle;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.stream.Stream;
import javax.swing.JButton;

/**
 * @author Daniel & Ana
 * PuzzleData,
 * This class load a puzzle's data such as clues, hints, correct solution,
 * labels, and back story
 * It also hold the user data that is set when a button on the board is clicked
 * 
 * The class provides helper methods to test if the board contains a valid 
 * solution; helper methods to clear errors off the board
 * 
 */
public class PuzzleData {
    

    // Atrbutes 
    // 2d arrays that holds all solutions.
    private int[] rightBoxSolution;
    private int[] leftBoxSolution;
    private int[] bottomBoxSolution;
    
    //2d arrays that holds user input.
    private int[][] rightBoxUserInput;
    private int[][] leftBoxUserInput;
    private int[][] bottomBoxUserInput;
    
    //Labels
    private String[] leftLabel;
    private String[] topLeftLabel;
    private String[] bottomLabel;
    
    //hints
    private ArrayList<Hint> hints;
    
    //clues
    private String[] clues;
    
    //backstory
    private String backstory;
    
    public class Hint
    {
        private String text;
        private String box; //left, right or bottom
        private int x, y;
        
        public Hint(String t, String bx, int x, int y)
        {
            text = t; box = bx; this.x = x; this.y = y;
        }
        
        public String getMessage()  {   return text; }
        public String getBox()      {   return box;  }
        public int getX()           {   return x;    }
        public int getY()           {   return y;    }
    }
    /**
	 * Construct PuzzleData with the default grid size 4x4.
	 */
    public PuzzleData(){
        
        rightBoxSolution = new int[4];
        leftBoxSolution = new int[4];
        bottomBoxSolution = new int[4];
        
        rightBoxUserInput = new int[4][4];
        leftBoxUserInput = new int[4][4];
        bottomBoxUserInput = new int[4][4];
        
        leftLabel = new String[4];
        topLeftLabel = new String[4];
        bottomLabel = new String[4];
        hints = new ArrayList<Hint>();

        clues = new String[6];
        backstory="";
        
    }
    
    /**
	 * Construct PuzzleData with specified grid size and clues.
	 * @param  grid size.
	 * @param cluesSize number of clues.
	 */
    public PuzzleData(int gridsize,int cluesSize ){
        
        rightBoxSolution = new int[gridsize];
        leftBoxSolution = new int[gridsize];
        bottomBoxSolution = new int[gridsize];
        
        rightBoxUserInput = new int[gridsize][gridsize];
        leftBoxUserInput = new int[gridsize][gridsize];
        bottomBoxUserInput = new int[gridsize][gridsize];
        
        leftLabel = new String[4];
        topLeftLabel = new String[4];
        bottomLabel = new String[4];
        hints = new ArrayList<Hint>();

        clues = new String[cluesSize];
        backstory="";
        
    }
    /**
     * Reads the puzzle data from the given file. File must be in the valid 
     * format,otherwise all field will be null.
     */
    public void readDB(String fileName) {
        if (checkFormat(fileName)) {// checking if the file format is valid or not.
            String line = null;
            int element = 0;// to prevent arrayindexoutofboundsexception
            BufferedReader buffereReader;
            try {
                buffereReader = new BufferedReader(new FileReader(fileName));

                while ((line = buffereReader.readLine()) != null
                        && !line.matches("/xEndOfFile")) {

                    if (line.matches("/xbackstory:")) {
                        while ((line = buffereReader.readLine()) != null
                                && !line.matches("/xhints:")) {
                            if (!line.isEmpty()) {
                                backstory += line + "\n";
                            }
                        }
                    }
                    
                    if (line.matches("/xhints:")) {
                        while (((line = buffereReader.readLine()) != null)
                                && !line.matches("/xclues:")) {
                            String text, box, x, y;
                            if(line.isEmpty())
                            {
                                System.out.println("Malformed Hint:");
                                break;
                            }                            
                            text = line.substring("HintText: ".length());
                            String temp = buffereReader.readLine();
                            temp = temp.substring("HintLocation: ".length());
                            String [] location = temp.split(", ");
                            box = location[0]; x = location[1]; y = location[2];
                            Hint h = new Hint(text, box, 
                                                    Integer.parseInt(x),
                                                    Integer.parseInt(y));
                            hints.add(h);
                        }
                        element = 0;
                    }
                    
                    if (line.matches("/xclues:")) {
                        while (((line = buffereReader.readLine()) != null)
                                && !line.matches("/xrightboxsolution:")) {
                            if (!line.isEmpty()) {
                                clues[element] = line;
                                element++;
                            }
                        }
                        element = 0;
                    }
                    
                    if (line.matches("/xrightboxsolution:")) {
                        while (((line = buffereReader.readLine()) != null)
                                && !line.matches("/xliftboxsolution:")) {
                            if (!line.isEmpty()) {
                                rightBoxSolution = Stream.of(line.split("-")).mapToInt(Integer::parseInt).toArray();
                            }
                        }
                    }
                    
                    if (line.matches("/xliftboxsolution:")) {
                        while (((line = buffereReader.readLine()) != null)
                                && !line.matches("/xbottomboxsolution:")) {
                            if (!line.isEmpty()) {
                                leftBoxSolution = Stream.of(line.split("-")).mapToInt(Integer::parseInt).toArray();
                            }
                        }
                    }
                    
                    if (line.matches("/xbottomboxsolution:")) {
                        while (((line = buffereReader.readLine()) != null)
                                && !line.matches("/xleftlabel:")) {
                            if (!line.isEmpty()) {
                                bottomBoxSolution = Stream.of(line.split("-")).mapToInt(Integer::parseInt).toArray();
                            }
                        }
                    }
                    
                    if (line.matches("/xleftlabel:")) {
                        while (((line = buffereReader.readLine()) != null)
                                && !line.matches("/xlefttoplabel:")) {
                            if (!line.isEmpty()) {
                                leftLabel[element] = line;
                                element++;
                            }
                        }
                        element = 0;
                    }
                    
                    if (line.matches("/xlefttoplabel:")) {
                        while (((line = buffereReader.readLine()) != null)
                                && !line.matches("/xbottomlabel:")) {
                            if (!line.isEmpty()) {
                                topLeftLabel[element] = line;
                                element++;
                            }
                        }
                        element = 0;
                    }
                    
                    if (line.matches("/xbottomlabel:")) {
                        while (((line = buffereReader.readLine()) != null)
                                && !line.matches("/xEndOfFile")) {
                            if (!line.isEmpty()) {
                                bottomLabel[element] = line;
                                element++;
                            }
                        }
                        element = 0;
                    }
                    
                }
                buffereReader.close(); //close file
            } catch (FileNotFoundException e) {
                System.out.println(e.getMessage());
            } catch (IOException e) {
                System.out.println(e.getMessage());
            } catch (ArrayIndexOutOfBoundsException e) {
                System.out.println(e.getMessage());
            } catch (NullPointerException e) {
                System.out.println(e.getMessage());
            }
        } else {
            System.out.println("File format is invalid");
        }

    }
    /**
     * This is a safety method only to check the given file is in the right format
     * or not. The goal is to prevent any errors in readDP method, which could 
     * lead to data to be loaded in the wrong field.
     * @param fileName that hold the puzzle data
     * @return true if file is valid, otherwise, will return false. 
     */
    public boolean checkFormat(String fileName) {
        String line = null;
        BufferedReader buffereReader;
        try {
            buffereReader = new BufferedReader(new FileReader(fileName));

            while ((line = buffereReader.readLine()) != null
                    && !line.matches("/xEndOfFile")) {

                if (line.matches("/xbackstory:")) {
                    while ((line = buffereReader.readLine()) != null
                            && !line.matches("/xEndOfFile")) {
                        if (line.matches("/xhints:")) {
                            while ((line = buffereReader.readLine()) != null
                                    && !line.matches("/xEndOfFile")) {
                                if (line.matches("/xclues:")) {
                                    while ((line = buffereReader.readLine()) != null
                                            && !line.matches("/xEndOfFile")) {
                                        if (line.matches("/xrightboxsolution:")) {
                                            while ((line = buffereReader.readLine()) != null
                                                    && !line.matches("/xEndOfFile")) {
                                                if (line.matches("/xliftboxsolution:")) {
                                                    while ((line = buffereReader.readLine()) != null
                                                            && !line.matches("/xEndOfFile")) {
                                                        if (line.matches("/xbottomboxsolution:")) {
                                                            while ((line = buffereReader.readLine()) != null
                                                                    && !line.matches("/xEndOfFile")) {
                                                                if (line.matches("/xleftlabel:")) {
                                                                    while ((line = buffereReader.readLine()) != null
                                                                            && !line.matches("/xEndOfFile")) {
                                                                        if (line.matches("/xlefttoplabel:")) {
                                                                            while ((line = buffereReader.readLine()) != null
                                                                                    && !line.matches("/xEndOfFile")) {
                                                                                if (line.matches("/xbottomlabel:")) {
                                                                                    while ((line = buffereReader.readLine()) != null) {
                                                                                        if (line.matches("/xEndOfFile")) {
                                                                                            return true;
                                                                                        }
                                                                                    }
                                                                                }
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }

                                                }
                                            }

                                        }
                                    }

                                }
                            }
                        }
                    }

                }
            }
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }
    
   
       
    /**
     * Returns hints at particular location (num), otherwise, will return null
     * if location does not exist.
     * @return the hints
     */
    public Hint getHint(int num) { 
        if(num<hints.size()){
            return hints.get(num);
        }
        else{
            return null;
        }
    }
    
    /**
     * Returns hints array size.
     * @return Hints size.
     */
    public int getHintSize() {
        return hints.size();
    }    

    /**
     * Returns clues array.
     * @return clues[]. 
     */
    public String[] getClues() {
        return clues;
    }
    
    /**
     * Checks user answers in all grids and buttons. 
     * @return true if user has all the right solutions,otherwise,returns false.
     */
    public boolean checkSolution()
    {
        if(!isSolved(rightBoxUserInput, rightBoxSolution))
            return false;
        
        if(!isSolved(leftBoxUserInput, leftBoxSolution))
            return false;
        
        if(!isSolved(bottomBoxUserInput, bottomBoxSolution))
            return false;
        
        return true;
    }
    /**
     * Used in checkSolution to check each grid and button.
     * @param input user input 2d array
     * @param solution solution array
     * @return  true if all solutions are correct. 
     */
    private boolean isSolved(int [][] input, int [] solution)
    {
        for(int i=0; i<input.length; i++)        
        {
            for(int j=0; j<input[i].length; j++)
                if(input[i][j] == 0)
                    return false;
        }
        
        for(int row=0; row<input.length; row++){
            for(int column=0; column<input[row].length; column++){
                if((input[row][column] == -1)&& solution[row]==column ){
                    return false;
                }
                else if((input[row][column] == 1)&& solution[row]!=column ){
                    return false;
                }
            }
        }        
        
        return true;
    }
    
    /**
     * Returns backstory.
     * @return backstory
     */
    public String getBackstory() {
        return backstory;
    }
    
    /**
      * Returns left labels.
     * @return leftLabel
     */
    public String[] getLeftLabel() {
        return leftLabel;
    }

    /**
     * Returns top left labels 
     * @return topLeftLabel
     */
    public String[] getTopLeftLabel() {
        return topLeftLabel;
    }

    /**
     * Returns bottom labels.
     * @return bottomLabel.
     */
    public String[] getBottomLabel() {
        return bottomLabel;
    }
    
     /**
      * Returns user input for left grid.
     * @return the leftBoxUserInput
     */
    public int[][] getLeftBoxUserInput() {
        return leftBoxUserInput;
    }

    /**
     * Returns user input for bottom grid.
     * @return the bottomBoxUserInput
     */
    public int[][] getBottomBoxUserInput() {
        return bottomBoxUserInput;
    }
    
    /**
     * Returns user input for right grid.
     * @return the rightBoxUserInput
     */
    public int[][] getRightBoxUserInput() {
        return rightBoxUserInput;
    }
    
    /**
     * Sets or change the user input to rightUserInput. 
     * @param int row, int column, int state to set
     */
    public void setRightBoxUserInput(int row, int column, int state) {
        rightBoxUserInput[row][column] = state;
    }

    /**
     * Sets or change the user input to leftUserInput.
     * @param int row, int column, int state to set
     */
    public void setLeftBoxUserInput(int row, int column, int state) {
        leftBoxUserInput[row][column] = state;
    }

    /**
     * Sets or change the user input to bottomUserInput.
     * @param int row, int column, int state to set
     */
    public void setBottomBoxUserInput(int row, int column, int state) {
        bottomBoxUserInput[row][column] = state;
    }

    /**
     * Checks and clear all user inputs from any errors.
     * @return number(s) of error(s) found, if any.
     */
    
    public int clearErrors(JButton[][] leftButtons,JButton[][] rightButtons,
            JButton[][] bottomButtons){
        
       int errors = 0;
        //right box
        for(int row=0; row<rightBoxUserInput.length; row++){
                    for(int column=0; column<rightBoxUserInput[row].length; column++){
                        if((rightBoxUserInput[row][column] == -1)&& rightBoxSolution[row]==column ){ // if the user has -1(false) in a box and the solution should be true
                            rightBoxUserInput[row][column]=0;
                            rightButtons[row][column].setText("");
                            errors++;
                        }
                        else if((rightBoxUserInput[row][column] == 1)&& rightBoxSolution[row]!=column ){
                            rightBoxUserInput[row][column]=0;
                            rightButtons[row][column].setText("");
                            errors++;
                        }
                    }
                }
        //left box
        for(int row=0; row<leftBoxUserInput.length; row++){
                    for(int column=0; column<leftBoxUserInput[row].length; column++){
                        if((leftBoxUserInput[row][column] == -1)&& leftBoxSolution[row]==column ){ // if the user has -1(false) in a box and the solution should be true
                            leftBoxUserInput[row][column]=0;
                            leftButtons[row][column].setText("");
                            errors++;
                        }
                        else if((leftBoxUserInput[row][column] == 1)&& leftBoxSolution[row]!=column ){
                            leftBoxUserInput[row][column]=0;
                            leftButtons[row][column].setText("");
                            errors++;
                        }
                    }
                }
        //bottom box
        for(int row=0; row<bottomBoxUserInput.length; row++){
                    for(int column=0; column<bottomBoxUserInput[row].length; column++){
                        if((bottomBoxUserInput[row][column] == -1)&& bottomBoxSolution[row]==column ){ // if the user has -1(false) in a box and the solution should be true
                            bottomBoxUserInput[row][column]=0;
                            bottomButtons[row][column].setText("");
                            errors++;
                        }
                        else if((bottomBoxUserInput[row][column] == 1)&& rightBoxSolution[row]!=column ){
                            bottomBoxUserInput[row][column]=0;
                            bottomButtons[row][column].setText("");
                            errors++;
                        }
                    }
                }
    
        return errors;
    }
    
    @Override 
    public boolean equals(Object obj){
        if(obj == this) return true;
        if(obj == null) return false;
        if(this.getClass() == obj.getClass()){
            PuzzleData puzzle = (PuzzleData) obj;
            return this.getBackstory() == puzzle.getBackstory();
    }
        else{ 
            return false;
}   
    }
}   