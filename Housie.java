package Housie;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.locks.ReentrantLock;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

interface WinningCondition{
	void checkWin(int ticket[][]);
}

class Row implements WinningCondition{
	static int conditionMet=0;
	static int row=0;
	int flag;
	public String displayRow(){
		if(row==0) {
			return "Top Row ";
		}
		else if(row==1) {
			return "Middle Row ";
		}
		else if(row==2) {
			return "Bottom Row ";
		}
		else 
			return "no row";
	}
	public int generateRow()
	{
		Random random=new Random();
		int rand=random.nextInt(3);
		return rand;
	}
	public void makeRow(){
		int newrow=generateRow();
		this.row=newrow;
	}
	
	public void checkWin(int ticket[][]) {
		if (conditionMet==0) {
			flag=0;
			for(int i=0;i<9;i++) {
				if(ticket[row][i]!=0&&ticket[row][i]!=100) {
					flag=1;
				}
			}
			if(flag==0) {
				conditionMet=1;
				System.out.println(" ***** CONGRATS! PLAYER HAS GOT ROW "+(row+1) +" ***** \n");
			}
		}
		
	}
}

class OtherWin implements WinningCondition{
	static int conditionMet=0;
	static int condn;
	 int counter;
	private int generateNum()
	{
		Random random=new Random();
		int rand=random.nextInt(3);
		return rand;
	}
	
	public void makeOtherWin(){
		condn=generateNum();
		}
	
	public String printCondition(){
		if(condn==0) {
			return "First five ";
		}
		else if(condn==1) {
			return "Corners ";
		}
		else if(condn==2) {
			return "Perimeter ";
		}
		else 
			return "none";
	}
	public void checkWin(int ticket[][]) {
		 int flag=0;
		if(conditionMet==0) {
			
			if(condn==0) {
				counter=0;
				//condition for first five
				for(int i=0; i<3;i++) {
					for(int j=0;j<9;j++) {
						if(ticket[i][j]==100) {
							counter++;
						}
					}
				}
				if(counter==5) {
					System.out.println(" ***** CONGRATS! PLAYER HAS GOT FIRST FIVE ***** \n");
					conditionMet=1;
				}
			}
			
			else if(condn==1){
				//corners
				if((ticket[0][0]==0||ticket[0][0]==100)&&(ticket[0][8]==0||ticket[0][8]==100) && (ticket[2][0]==0||ticket[2][0]==100) && (ticket[2][0]==0||ticket[2][8]==100)) {
					System.out.println(" ***** CONGRATS PLAYER HAS GOT THE CORNERS! ***** \n");
					conditionMet=1;
				}
				else {
					//System.out.println("no corners :( ");
				}
			}
			
			else if(condn==2) {
				//perimeter
				for(int i=0;i<3;i+=2) {
					for(int j=0; j<9;j++) {
						if((ticket[i][j]!=0&&ticket[i][j]!=100) || (ticket[1][0]!=0&&ticket[1][0]!=100) || (ticket[1][8]!=0&&ticket[1][8]!=100) ) {
							flag=1;
						}
					}
				}
				
				if(flag==0) {
					conditionMet=1;
					System.out.println(" ***** CONGRATS PLAYER HAS GOT THE PERIMETER ***** \n");
				}
				else {
					//System.out.println("no perimeter :( ");
				}
			}
		}
	}
}

class FullHouse implements WinningCondition{
	static int conditionMet=0;
	private int flag=0;
	public void checkWin(int ticket[][]){
		if(conditionMet==0){
			flag=0;
			for(int i=0; i<3;i++){
				for(int j=0;j<9;j++){
					if(ticket[i][j]!=0 && ticket[i][j]!=100){
						flag=1;
					}
				}
			}
			if(flag==0) {
				System.out.println(" ***** FULL HOUSE!! PLAYER HAS WON ***** ");
				conditionMet=1;
				System.out.println("Game over! ");
				System.exit(0);
			}
		}
		
		
	}
}

class GameData{
	static int gameTurn=0;
	static ArrayList<Integer> access=new ArrayList<Integer>();
	static boolean checkConditions=false;
	
	//ReentrantLock lock= new ReentrantLock();
	
	Row row=new Row();
	OtherWin otherWin= new OtherWin();
	FullHouse fullHouse=new FullHouse();
	
	public void displayWinningConditions() {
		row.makeRow();
		otherWin.makeOtherWin();
		System.out.println("The winning conditions are: ");
		System.out.println("1. Row: " + row.displayRow()+" \n2. "+ otherWin.printCondition()+" \n3. Full House ");
		System.out.println();
	}
	
	public void checkConditions(int ticket[][]) {
		row.checkWin(ticket);
		otherWin.checkWin(ticket);
		fullHouse.checkWin(ticket);
		
	}
}
class Moderator{
	int noOfPlayers=-1;
	int gameOver =0;
	int count=0;
	ArrayList<Integer> numList= new ArrayList<Integer>();
	//public void generateNumber();
	Moderator(){
		generateList();
	}
	public int generateNumber()
	{
		int num= numList.get(count);
		count++;
		if(count==1) {
			numList.add(num);
			return 0;
		}
		System.out.println("\n --------- THE NUMBER GENERATED IS "+num+"  ----------" +"\n");
		return num;
	}
	public void generateList() {
		for(int i=1;i<91;i++) {
			numList.add(i);
		}
		Collections.shuffle(numList);
	}
	
	public void mainMenu() {
		System.out.println("-------------------------------------------------------------\n\n\n");
		System.out.println("               WELCOME TO THE GAME OF HOUSIE !                \n\n\n");
		System.out.println("-------------------------------------------------------------\n\n\n");
		System.out.println("\nAre you ready to play? Press enter to continue ");
		Scanner in=new Scanner(System.in);
		String str=in.nextLine();
		System.out.println("\n");
	}
	
}

class Player implements Runnable{
	//String name;
	ReentrantLock lock;
	Thread t;
	static int numberAnnounced;
	int pid;
	boolean suspendFlag=true;
	int[][] ticket= {
			{0,0,0,0,0,0,0,0,0},
			{0,0,0,0,0,0,0,0,0},
			{0,0,0,0,0,0,0,0,0}
	};
	GameData gameData=new GameData();

	static int turn;
	private void generateTicket() {
		ArrayList<Integer> num= new ArrayList<Integer>();
		for(int i=0;i<3;i++) {
			int rand=ThreadLocalRandom.current().nextInt(1,9); 
			if(num.contains(rand)) {
				i--;
			}
			else 
				num.add(rand);
		}
		ArrayList<Integer> size= new ArrayList<Integer>();
		
		for(int i=0;i<9;i++) {
			if(num.contains(i)) {
				size.add(1);
			}
			else 
				size.add(2);
		}
		num.clear();
		int genNum,temp;
		int pos=0;
		int oldpos=0;
		
		for(int i=0;i<9;i++) {
			
			for(int j=0;j<size.get(i);j++) {
				
				pos=ThreadLocalRandom.current().nextInt(0,3);
				while(pos==oldpos) {
					pos=ThreadLocalRandom.current().nextInt(0,3);
				}
				genNum=ThreadLocalRandom.current().nextInt(i*10,(i+1)*10);
				for(int s=0;s<3;s++) {
					while(ticket[s][i]==genNum) {
						genNum=ThreadLocalRandom.current().nextInt(i*10,(i+1)*10);
					}
				}
				ticket[pos][i]=genNum;
				oldpos=pos;
				pos=0;
				
			}
			
			for(int k=0;k<3;k++) {
				if(ticket[k][i]!=0) {
					for(int l=k;l<3;l++) {
						if(ticket[l][i]<ticket[k][i]&&ticket[l][i]!=0&&ticket[k][i]!=0) {
							temp=ticket[l][i];
							ticket[l][i]=ticket[k][i];
							ticket[k][i]=temp;
						}
					}
				}
			}
			oldpos=0;
			pos=0;
		}
		System.out.println("The ticket generated for player "+(pid+1)+" is: ");
		displayTicket();

	}
	
	Player(int pid){
		//this.turn=turn;
		this.pid=pid;
		t=new Thread(this);
		//System.out.println(name+" is ready to play !!");
		//this.name=name;
		generateTicket();
	}

	public void displayTicket() {
		System.out.println("");
		for(int i=0;i<3;i++) {
			for(int j=0;j<9;j++) {
				System.out.printf("%4d",ticket[i][j]);
			}
			System.out.println("");
		}
		System.out.println("");

	}
	
	synchronized public void checkNumber() {
		
		//System.out.println("checking the number of player "+pid);		
		//System.out.println(numberAnnounced +" is number announced ");
		int flag=0;
		for(int i=0;i<3;i++) {
			for(int j=0;j<9;j++) {
				if(ticket[i][j]==numberAnnounced) {
					System.out.println(ticket[i][j]+" is found! ");
					ticket[i][j]=100;
					flag=1;
				}
			}
		}
		if(flag==0) {
			System.out.println("Number not found ");
		}
		System.out.println("Displaying the Ticket for player " +(pid+1));
		displayTicket();

	}
	
	
	@SuppressWarnings("static-access")
	public void run() {
		try {
			t.sleep(200);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			while(true) {
				while(gameData.gameTurn!=pid) {
					t.sleep(20);
				}
				
				if(gameData.gameTurn==pid) {
					//gameData.access.set(pid,0);
					checkNumber();
					gameData.checkConditions(ticket);
					gameData.gameTurn++;
					//gameData.access.set(pid,1);
					//gameData.lock.notifyAll();
				}
			}
		} catch(Exception e){
			System.out.println("printing exception");
		}
	}
	
}

public class Housie {

	@SuppressWarnings("removal")
	public static void main(String[] args) throws InterruptedException {
		GameData gameData=new GameData();
		Moderator moderator = new Moderator();
		//ReentrantLock lock=new ReentrantLock();
		//int turn =0;
		moderator.mainMenu();
		gameData.displayWinningConditions();
		@SuppressWarnings("resource")
		Scanner in=new Scanner(System.in);

		System.out.println("Enter number of players ");
		moderator.noOfPlayers=in.nextInt();
		
		while(moderator.noOfPlayers<2||moderator.noOfPlayers>6) {
			System.out.println("Enter number of players [2-6] ");
			moderator.noOfPlayers=in.nextInt();
		}
		ArrayList<Player> player= new ArrayList<Player>();
		
		for(int i=0;i<moderator.noOfPlayers;i++) {
			Player p=new Player(i);
			player.add(p);
			GameData.access.add(0);
			player.get(i).t.start();
		}
		int numAnnounced=0;
		Thread.currentThread().sleep(10);

		while(moderator.gameOver==0) {
			numAnnounced=moderator.generateNumber();
			Player.numberAnnounced=numAnnounced;	
			if(GameData.gameTurn==moderator.noOfPlayers) {	
				GameData.gameTurn=0;
			}
			//press enter
			String str=in.nextLine();
	}

}
}

class HousieSwing {

	private JFrame frame;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					HousieSwing window = new HousieSwing();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public HousieSwing() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.getContentPane().setBackground(Color.CYAN);
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Welcome! Are you ready to play?");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(10, 20, 416, 23);
		frame.getContentPane().add(lblNewLabel);
		
		JButton btnNewButton = new JButton("Yes! Continue.");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				lblNewLabel.setText("Great.");
			}
		});
		btnNewButton.setBackground(Color.BLUE);
		btnNewButton.setBounds(98, 66, 121, 23);
		frame.getContentPane().add(btnNewButton);
		
		JButton btnNewButton_1 = new JButton("No. Exit.");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				lblNewLabel.setText("To exit the game click on the X at the top right corner.");
			}
		});
		btnNewButton_1.setBackground(Color.BLUE);
		btnNewButton_1.setBounds(229, 66, 89, 23);
		frame.getContentPane().add(btnNewButton_1);
		
		
		
		
		
		
	}
}
