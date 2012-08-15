import core.*;
import java.awt.*;
import javax.swing.*;
import java.util.*;

public class HandSample4 extends JFrame{
    
  private Deck myDeck;
  private Card myCard;
  
  private final int NUMBER_OF_HANDS = 4;
	private final int SIZE_OF_HAND = 13;
	private final int BETWEEN_CARDS_WIDTH = 26; /* 26 is good. 12 pixels to hide pictures*/
	private final int WIDTH = 1024;
	private final int HEIGHT = 768;
	private final int B_X = 10;
	private final int B_Y = 600;
	private final int NORTH_X = 1024/2 - BETWEEN_CARDS_WIDTH * 7; /* Ideal would be 6,5 (half of the cards) */
	private final int SOUTH_X = NORTH_X;
	private final int EAST_X = NORTH_X + 250;
  private final int WEST_X = NORTH_X - 250;
  private final int NORTH_Y = 20;
	private final int SOUTH_Y = 550;
	private final int EAST_Y = 300;
	private final int WEST_Y = EAST_Y;
	private final int CARD_WIDTH = 72;
	private final int CARD_HEIGHT = 96;
	
  private int turn;
	public int[] initial_x = new int[4];
  public int[] initial_y = new int[4];
  public int[] light_x = new int[4];
  public int[] light_y = new int[4];
	
  private Hand[] hands = new Hand[NUMBER_OF_HANDS];
	private JButton[][] buttons = new JButton[NUMBER_OF_HANDS][ SIZE_OF_HAND ]; /*Don't know how not to use this.*/
	private JButton[][] handButtons = new JButton[NUMBER_OF_HANDS][ SIZE_OF_HAND ];
	
	javax.swing.JButton JButton1 = new javax.swing.JButton();
	javax.swing.JButton turn_light = new javax.swing.JButton();
	private java.awt.Color TABLE_COLOR = new java.awt.Color(0,100,0);
	private java.awt.Color TURN_LIGHT_COLOR = new java.awt.Color(255,0,0);
	
	public static void main (String args[]) {
	  HandSample4 sample = new HandSample4();
	  sample.init();
	  sample.setVisible(true);
	}
	
	public void init(){
	
	initial_y[0]=NORTH_Y;
	initial_y[1]=EAST_Y;
  initial_y[2]=SOUTH_Y;
	initial_y[3]=WEST_Y;
	initial_x[0]=NORTH_X;
	initial_x[1]=EAST_X;
  initial_x[2]=SOUTH_X;
	initial_x[3]=WEST_X;  
	
  light_y[0]=NORTH_Y;
	light_y[1]=EAST_Y;
  light_y[2]=SOUTH_Y;
	light_y[3]=WEST_Y;
	light_x[0]=NORTH_X;
	light_x[1]=EAST_X;
  light_x[2]=SOUTH_X;
	light_x[3]=WEST_X;
	
		
		// This line prevents the "Swing: checked access to system event queue" message seen in some browsers.
		getRootPane().putClientProperty("defeatSystemEventQueueCheck", Boolean.TRUE);
		
		getContentPane().setLayout(null);
		getContentPane().setBackground(TABLE_COLOR);
		setSize(WIDTH,HEIGHT);
		
		/*Cards*/
		for(int k=0; k<NUMBER_OF_HANDS; k++){
	    for(int i=SIZE_OF_HAND-1;i>=0;i--){ /*Only way I figured to paint in order*/
		    buttons[k][i] = new JButton();
		
		    buttons[k][i].setFocusPainted(false);
	      buttons[k][i].setRolloverEnabled(false); /*Does not bring up the focused button*/
      	buttons[k][i].setBorderPainted(false); /*Does not paint the border*/
      	buttons[k][i].setContentAreaFilled(false); /*Paint always in the same order???*/
		
		    buttons[k][i].setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		    buttons[k][i].setToolTipText("This is a card.");
		    getContentPane().add(buttons[k][i]);		
		    buttons[k][i].setBounds(initial_x[k] + i*BETWEEN_CARDS_WIDTH,initial_y[k],CARD_WIDTH,CARD_HEIGHT); 
		    // JLabel array mapping
  		  handButtons[k][i] = buttons[k][i]; /*Really didn't understand the need of this line but break without it */
	    }
	  }
	  
	turn = 0;
	turn_light.setBounds(initial_x[turn]-10 , initial_y[turn]-10 , (SIZE_OF_HAND-1)*BETWEEN_CARDS_WIDTH + CARD_WIDTH + 10 + 10, CARD_HEIGHT+20);
	//turn_light.setBorderPainted(true); 
	turn_light.setRolloverEnabled(false);
	turn_light.setBackground(TURN_LIGHT_COLOR);
	turn_light.setOpaque(true);
	turn_light.setContentAreaFilled(true);
		getContentPane().add(turn_light);
	
		
		/*Other buttons*/
		JButton1.setText("Draw and Sort");
		JButton1.setActionCommand("Draw and Sort");
		getContentPane().add(JButton1);
		JButton1.setBounds(WIDTH/2 - 130,HEIGHT - 80,260,30);
		
		/*Initializing Hand*/
		myDeck = new Deck();
		for(int k=0; k<NUMBER_OF_HANDS; k++){
		  hands[k] = new Hand();
		}
    deal();
		display();
		
		/*Listeners*/	
		SymAction lSymAction = new SymAction();
		JButton1.addActionListener(lSymAction);
		for(int k=0; k<NUMBER_OF_HANDS; k++){
		  for(int i=0;i<SIZE_OF_HAND;i++){
			  handButtons[k][i].addActionListener(lSymAction);
		  }
		}
	}

	class SymAction implements java.awt.event.ActionListener{
		public void actionPerformed(java.awt.event.ActionEvent event){
			Object object = event.getSource();
			if (object == JButton1){
					draw(event);
					sort(event);
			}
			else{
			  for(int k=0; k<NUMBER_OF_HANDS; k++){
				  for(int i=0;i<SIZE_OF_HAND;i++){
					   if (k==turn && object == handButtons[k][i]){
						  removeCard(event,k,i);
						  moveTurn();
						  displayTurn();
						 }
				  }
				}
			}
			display();
		}
	}

	private void draw(java.awt.event.ActionEvent event){
	  for(int k=0; k<NUMBER_OF_HANDS; k++)
	    hands[k].discardHand();
		myDeck.restore();
		myDeck.shuffle();
		restoreButtons();
		deal();

	}

	private void sort(java.awt.event.ActionEvent event){
		for(int k=0; k<NUMBER_OF_HANDS; k++)
	    hands[k].sort();
	}
	
	private void removeCard(java.awt.event.ActionEvent event, int hand, int id){
		int last;
		last = hands[hand].getNumberOfCards()-1;
		if(last>=id){
			hands[hand].removeCard(id);
			handButtons[hand][last].setVisible(false);
		}
	}
	private void restoreButtons(){
	  for(int k=0; k<NUMBER_OF_HANDS; k++)
		  for(int i=0;i< SIZE_OF_HAND;i++){
			  handButtons[k][i].setVisible(true);
	    }
	}
	
	private void deal(){
		Card card;
		for(int k=0; k<NUMBER_OF_HANDS; k++)
		  for(int i=0;i< SIZE_OF_HAND;i++){
          	card = myDeck.dealCard();
          	hands[k].addCard( card );
	      }
	}
	
	private void display(){
		int cards;
		int discarded; /*Number of cards already discarded*/
		for(int k=0; k<NUMBER_OF_HANDS; k++){
		  cards = hands[k].getNumberOfCards();
		  discarded = SIZE_OF_HAND - cards;
	     for (int i = 0; i <cards; i++ ) {
		      Card c = hands[k].getCard( i );
		      handButtons[k][i].setIcon( c.getCardImage() );
		      handButtons[k][i].setBounds(initial_x[k] + i*BETWEEN_CARDS_WIDTH + discarded*BETWEEN_CARDS_WIDTH/2,initial_y[k],CARD_WIDTH,CARD_HEIGHT);
		  }
		}
	}
	
	private void moveTurn(){
	  turn++;
	  if(turn==4)
	    turn=0;
	  turn_light.setBounds(light_x[turn]-10,light_y[turn]-10,20,20);
	    
	}
	
	private void displayTurn(){
	    int cards = hands[turn].getNumberOfCards();
		  int discarded = SIZE_OF_HAND - cards;
		  turn_light.setBounds(initial_x[turn]-10 + discarded*BETWEEN_CARDS_WIDTH/2 ,initial_y[turn]-10,(cards-1)*BETWEEN_CARDS_WIDTH + CARD_WIDTH + 10 + 10,CARD_HEIGHT+20);
	}
	
}

