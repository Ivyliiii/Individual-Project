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

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class Soduku {
	
	public final int width = 800, height = 1000;
	public JTextArea displayArea;
	public final int buttonWidth = 60, buttonHeight = 50;
	int pressed_x, pressed_y, released_x, released_y;
	int canvasHeight = height*2/3;
	ArrayList<Rect> shapes = new ArrayList<Rect>();

	public Soduku() {
		JFrame frame = new JFrame();
		frame.setSize(width, height);
		JPanel canvas = new JPanel() {
			public void paint(Graphics g) {
				// draying the text display
				g.setColor(Color.WHITE);
				g.fillRect(0, 0, width, 2*height/3);
				int mid = 20;
				for(int i = 0; i < 9; i++) {
					Rect rect = new Rect(mid + 80*i, canvasHeight + mid, buttonWidth, buttonHeight, new Color(250, 217, 132), g);
					shapes.add(rect);
				}
			}
		};
		
		JPanel firstRow = new JPanel() {};
		firstRow.setPreferredSize(new Dimension(width*5/6, canvas.getHeight()-10));
		
		JPanel secondRow = new JPanel();
		secondRow.setPreferredSize(new Dimension(width*5/6, canvas.getHeight()-10));

		JPanel thirdRow = new JPanel();
		thirdRow.setPreferredSize(new Dimension(width*5/6, canvas.getHeight()-10));
		
		JPanel left1Box = new JPanel();
		
		JPanel middle1Box = new JPanel();
		 
		JPanel right1Box = new JPanel();
		
		JPanel left2Box = new JPanel();
		
		JPanel middle2Box = new JPanel();
		
		JPanel right2Box = new JPanel();
		
		JPanel left3Box = new JPanel();
		
		JPanel middle3Box = new JPanel();
		
		JPanel right3Box = new JPanel();
		
		BoxLayout boxlayout = new BoxLayout(canvas, BoxLayout.Y_AXIS); // makes sure that the layout builds from the top to bottom
		canvas.setLayout(boxlayout);
		
		frame.add(canvas);
		frame.setVisible(true);
		firstRow.add(left1Box);
		firstRow.add(middle1Box);
		firstRow.add(right1Box);
		secondRow.add(left2Box);
		secondRow.add(middle2Box);
		secondRow.add(right2Box);
		thirdRow.add(left3Box);
		thirdRow.add(middle3Box);
		thirdRow.add(right3Box);
		canvas.add(firstRow);
		canvas.add(secondRow);
		canvas.add(thirdRow);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
	
	}
	
	public int[][] generateSoduku(int difficulty){
		int[][] a = new int[9][9];
		int count = 0;
		while(count <= 80) {
			int x = randomNum();
			int y = randomNum();
			if(a[x][y] == 0) {
				int fill = randomNum()+1;
				boolean couldFill = true;
				for(int i = 0; i < a.length; i++) {
					if(a[i][y] == fill) {
						couldFill = false;
					}
				}
				for(int j = 0; j < a[x].length; j++) {
					if(a[x][j] == fill) {
						couldFill = false;
					}
				}
				if(couldFill) {
					a[x][y] = fill;
					if(solveSoduku(a)) {
						return a;
					}
					System.out.println("Why");
					count++;
				}
			}
		}
		return null;
	}
	
	public void removeBlock() {
		
	}
	
	public int randomNum() {
		return (int)Math.random()*9;
	}
	
	public boolean solveSoduku(int[][] arr) {
		ArrayList<ArrayList<Integer>> possibleNums = new ArrayList<ArrayList<Integer>>();
		ArrayList<ArrayList<Integer>> prev = new ArrayList<ArrayList<Integer>>();
		boolean contin = true;
		boolean same = false;
		boolean first = true;
		while(contin == true && !same) {
			for(int i = 0; i < arr.length; i++) {
				for(int j = 0; j < arr[i].length; j++) {
					if(arr[i][j] == 0) {
						possibleNums.add(checkPosible(arr,i,j));
					}
					else {
						possibleNums.add(new ArrayList<Integer>());
					}
					if(first) {
						prev.add(new ArrayList<Integer>());
					}
				}
				contin = false;
				same = true;
				int count = 0;
				for(ArrayList<Integer> list:possibleNums) {
					if(list.size() != 1 && list.size() != 0) {
						contin = true;
					}
					if(prev.get(count).size() != list.size()) {
						same = false;
						System.out.println("Same");
					}
					count++;
				}
				prev = possibleNums;
			}
			first = false;
		}
		if(contin == false) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public ArrayList<Integer> checkPosible(int[][] arr, int i, int j){
		ArrayList<Integer> out = new ArrayList<Integer>();
		for(int index = 1; index <= 9; index++) {
			out.add(index);
		}
		for(int n = 0; n < arr.length; n++) {
			if(out.contains(arr[n][j])) {
				out.remove(arr[n][j]);
			}
		}
		for(int index = 0; index < arr[i].length; index++) {
			if(out.contains(arr[i][index])) {
				out.remove(arr[i][index]);
			}
		}
		System.out.println(out.size());
		return out;
	}
	
	public static void main(String[] args) {
		Soduku run = new Soduku();
		run.generateSoduku(1);
		System.out.println("finished");
	}
	
}
