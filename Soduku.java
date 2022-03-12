import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.AbstractButton;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.util.*;
import static javax.swing.JOptionPane.showMessageDialog;

public class Soduku { // Basic soduku game that I hope to implement more in the future. The methods for generating a soduku is given 
	// a person on quora, and here is the link: https://www.quora.com/How-do-I-create-a-program-that-generates-sudoku-puzzles-in-Java
	// thanks to jason for finding so many useful sources for me
	// these are some boring variables 
	public final int width = 800, height = 1000;
	public JTextArea displayArea;
	public JPanel userArea, container, board;
	public final int buttonWidth = 60, buttonHeight = 50;
	public JPanel userButtons;
	int selected = 0; // this keeps track of the number that the user is trying to fill in with
	int difficulty = 1; // this part of the program is really easy to redo, but I am struggling to repain the sreen
	int life = 3; // the number of chances the user has to make mistakes
	JButton[][] grid;
	int[][] g = new int[9][9];
	public Soduku() {
		JFrame frame = new JFrame();
		frame.setSize(800,1000);	
		frame.setBackground(Color.GRAY);
		JPanel container = new JPanel() {};
		board = new JPanel() {};
		
		board.setPreferredSize(new Dimension(width, height*14/7));
		container.setSize(width, height);
		
		JPanel userArea = new JPanel();
		userArea.setPreferredSize(new Dimension(width, 4*height/14));
		userArea.setBackground(Color.WHITE);
		
		int[][] solution = generateSoduku(); // calls the method to generate a fully filled soduku
		int[][] temp = new int[9][9]; // make an exact copy of solution
		for(int i = 0; i < solution.length; i++) {
			for(int j = 0; j < solution[i].length; j++) {
				temp[i][j] = solution[i][j];
			}
		}
		
		// take out a certain amout of soduku blocks based on the difficulty
		int[][] soduku = takeOut(temp, difficulty);
		
		grid = new JButton[9][9];
		for(int i = 0; i < grid.length; i++) {
			for(int j = 0; j < grid[i].length; j++) {
				// fills the individual buttons in based on the soduku models that we got previously
				if(soduku[i][j] != 0) {
					grid[i][j] = new JButton(String.valueOf(soduku[i][j]));
				}
				else { // if the blco kis supposed to be kept blank, the buttn will display nothing
					grid[i][j] = new JButton(" ");
					grid[i][j].setHideActionText(false);
				}
				board.add(grid[i][j]);
				grid[i][j].setPreferredSize(new Dimension(80, 60));
				grid[i][j].addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						int row = 0;
						int col = 0;
						// this gets the exact button that I am looking at out of the list of buttons
						JButton button = (JButton) e.getSource();
						// find the location of the button, so we can knwo which number it corresponds to in the solution
						for(int i = 0; i < grid.length; i++) {
							for(int j = 0; j < grid[i].length; j++) {
								if(grid[i][j] == button) {
									row = i;
									col = j;
								}
							}
						}
						// the block is set to the number that the user thinks should go there
						button.setText("" + selected);
						// if it is incorrect
						if(!((button.getText().equals(String.valueOf(solution[row][col]))))) {
							life--;
							// print an error message and fill in the correct answer for the user
							showMessageDialog(null, "That is not the correct answer! You put in " + button.getText() + " but "
									+ "the correct answer is actually " + solution[row][col] + "\n You have " + life + " live left!");
							button.setText(solution[row][col] + "");
							frame.getContentPane().repaint();
							// game will be over if the user has no time left
							if(life == 0) {
								showMessageDialog(null, "Game Over");
								System.exit(0);
							}
						}
					}
				});
			}
		}
		
		// more buttons to make that will change the difficulty and take out more squares when making the soduku from the answers
		JButton Difficulty1 = new JButton("Level 1");
		Difficulty1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				difficulty = 1;
				frame.getContentPane().repaint(); // i was unable to repaint the whole thing based o nthe difficulty
			}
		});
		
		JButton Difficulty2 = new JButton("Level 2");
		Difficulty2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				difficulty = 2;
				frame.getContentPane().repaint();
			}
		});
		
		JButton Difficulty3 = new JButton("Level 3");
		Difficulty3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				difficulty = 3;
				frame.getContentPane().repaint();
			}
		});
		
		JTextArea lives = new JTextArea();
		
		//another jpanel that keeps the buttons to change the difficulty and display the number of lives
		JPanel userButtons = new JPanel() {};
		userButtons.setPreferredSize(new Dimension(width, height/8));
		
		JTextArea difficultyArea = new JTextArea() {};
		
		// i also coult not get the number of hearts to change based on the lives the user has left, but this should be an easy fix
		lives.setText("        Lives Left: ");
		for(int i = 0; i < life; i++) {
			lives.append("\u2665"); // I still have to figure out how I will be able to change the number of hearts when the user 
			// make mistakes 
		}
		lives.setOpaque(false);
			
		difficultyArea.setText("Difficulty: ");
		difficultyArea.setEditable(false);
		difficultyArea.setOpaque(false);
		
		// the buttons that will be displayed at the botton of the screen with 9 numbers
		JButton[] numbers = new JButton[9];
		JButton num1 = new JButton("1");
		num1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selected = 1; // the last selected button will be number that gets filled in when the user presses on a square
			}
		});
		numbers[0] = num1;
		JButton num2 = new JButton("2");
		num2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selected = 2;
			}
		});
		numbers[1] = num2;
		JButton num3 = new JButton("3");
		num3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selected = 3;
			}
		});
		numbers[2] = num3;
		JButton num4 = new JButton("4");
		num4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selected = 4;
			}
		});
		numbers[3] = num4;
		JButton num5 = new JButton("5");
		num5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selected = 5;
			}
		});
		numbers[4] = num5;
		JButton num6 = new JButton("6");
		num6.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selected = 6;
			}
		});
		numbers[5] = num6;
		JButton num7 = new JButton("7");
		num7.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selected = 7;
			}
		});
		numbers[6] = num7;
		JButton num8 = new JButton("8");
		num8.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selected = 8;
			}
		});
		numbers[7] = num8;		
		JButton num9 = new JButton("9");
		num9.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selected = 9;
				frame.getContentPane().repaint();
				
			}
		});
		numbers[8] = num9;
		userArea.add(num9);
		
		// basic settings for the 9 number buttons
		for(int i = 0; i < numbers.length; i++) {
			if(i != selected -1) {
				numbers[i].setPreferredSize(new Dimension(80, 80));
				numbers[i].setBackground(new Color(255, 238, 199));
				numbers[i].setOpaque(true);
				numbers[i].setBorderPainted(false);
			}
			userArea.add(numbers[i]);
		}
		
		// this will make sure that the overall layout of the page is vertical
		BoxLayout boxlayout = new BoxLayout(container, BoxLayout.Y_AXIS); // makes sure that the layout builds from the top to bottom
		container.setLayout(boxlayout);
		// additing everything on to the dispay area
		container.add(board);
		userButtons.add(difficultyArea);
		userButtons.add(Difficulty1);
		userButtons.add(Difficulty2);
		userButtons.add(Difficulty3);
		userButtons.add(lives);
		container.add(userButtons);
		container.add(userArea);
		frame.add(container);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	
	// main method for generating soduku
	public int[][] generateSoduku(){
		boolean ran = true; // boolean to test if any number could go in a spot
		ArrayList<int[]> arr= new ArrayList<int[]>(); // an arrayList of randomized lists of 1-9
		for(int i = 0; i < 81; i++) {
			arr.add(randomList());
		}
		int times = 0; // this keeps the number of times that the program has been running in each iteration of while loop, so 
		// if the program runs too many times, we can just rerun it since it is never going to form a valid soduku
		int filled = 0; // numbers of blocks filled in
		while(filled < 81) { // before everything is filled
			ran = false; // start with nothing was run
			for(int i = 0; i < arr.get(filled).length; i++) {
				if(arr.get(filled)[i] != 0) { // since we turn all numbers in a randomized list of 1-9 after it has been used, we find the next one that has not been used
					g[filled/9][filled%9] = arr.get(filled)[i]; // fill it in
					arr.get(filled)[i] = 0; // turn the number to 0 in the randomized list as they have been tested
					times++;
					if(valid(filled/9, filled%9)){ // chekcs to see if this filled location creates conflicts
						ran = true; // a number worked in that locaiton
						filled++; // another block was filled
						break; // move on to the next iteration of while loop
					}
				}
			}
			if(times > 1000) { // if times is too high, we just run the program again
				g = new int[9][9]; // reset everything
				times = 0;
				return generateSoduku(); // return the result if something we want is obtained
				
			}
			if(!ran && filled != 0) { // if no values could work for a blcok
				arr.set(filled, randomList()); // we make it so that the we have not tried this block and return to the previous block
				filled--;
			}
			else if(!ran && filled == 0) { // if we are at the start of the game and we still cannot run anothing thing
				times = 0; // we will just rerun the program
				g = new int[9][9];
				return generateSoduku();
			}
		}
		printSoduku(g); // print the grid for the user
		return g; // returns the generated soduku
	}
	
	// function that will take out a certain number of bumbers based on difficulty
	public int[][] takeOut(int[][] g, int difficulty) {
		for(int i = 0; i < g.length; i++) {
			for(int j = 0; j < g[i].length; j++) {
				// means the possibility of getting removed it higher as difficulty increases
				if(Math.random() < 1-Math.pow(0.6, difficulty)) {
					g[i][j] = 0;
				}
			}
		}
		return g;
	}
	
	// prints the whole soduku out n console so coders can see them well
	public void printSoduku(int[][] soduku) {
		for(int r = 0; r < soduku.length; r++) {
			for(int c = 0; c < soduku[r].length; c++) {
				System.out.print(soduku[r][c] + "  ");
			}
			System.out.println();
		}
	}
	
	// checks to see if a certain location for a number creates contradiction with numbers in that row, colemn
	public boolean valid(int row, int col) {
		for(int i = 0; i < g.length; i++) {
			// checks in the column
			if(i!=col) {
				System.out.println( i + "  "+ col);
				if(g[row][i] == g[row][col]) {
					return false;
				}
			}
			//checks in the row
			if(i != row) {
				if(g[i][col] == g[row][col]) {
					return false;
				}
			}
		}
		//checks in the 3x3 block that the number lies in
		for(int i = row/3 * 3; i < row/3*3+3; i++) {
			for(int j = col/3*3; j < col/3*3+3; j++) {
				if(i != row && j != col) {
					if(g[i][j] == g[row][col]){
						return false;
					}
				}
			}
		}
		return true;
	}
	
	// this will give a list of numbers from 1-9 in a random order; this is part of the quora's plan
	public int[] randomList() {
		int[] arr = new int[9];
		for(int i = 1; i <= 9; i++) {
			arr[i-1] = i;
		}
		// changes the location of the numbers with each other
		for(int i =  0; i < arr.length; i++) {
			int index = randomNum();
			int num = arr[index];
			int index2 = randomNum();
			arr[index] = arr[index2];
			arr[index2] = num;
		}
		return arr;
	}
	
	// generates a random number between 1-9
	public int randomNum() {
		return (int)(Math.random()*9);
	}
	
	public static void main(String[] args) {
		Soduku run = new Soduku();
	}
	
}
