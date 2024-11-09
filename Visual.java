import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;
public class Visual implements ActionListener, KeyListener, Constants {
	private JFrame frame; //REQUIRED! The outside shell of the window
	public DrawingPanel panel; //REQUIRED! The interior window
	private Timer visualtime; //REQUIRED! Runs/Refreshes the screen.
	//Adjust these values:

	//All other public data members go here:
	//public ArrayList<Dino> dinos;
	Dino dino;
	public ArrayList<Cactus> cacti;
    public boolean paused;
    public static int counter=0;
    public int score;
    public int bestscore;
    public int gen;
    public int bestgen;
    public ArrayList<Dino> dinos;
    public boolean saving;
    public boolean loading;	
	
	public Visual()
	{
		//Adjust the name, but leave everything else alone.
		frame = new JFrame("Dinosaur Game... ");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		panel = new DrawingPanel();
		panel.setPreferredSize(new Dimension(WIDE, HIGH));
		frame.getContentPane().add(panel);
		panel.setFocusable(true);
		panel.requestFocus();
		panel.addKeyListener(this);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		Initialize();
		bestgen = 0;
		bestscore=0;
		//This block of code is fairly constant too -- always have it.
		visualtime = new Timer(20, this);
		visualtime.start();
		}
		public void Initialize()
		{
		    cacti=new ArrayList<Cactus>();
		    cacti.add(new Cactus((int)(WIDE*.52)));
		    cacti.add(new Cactus(WIDE+10));
		    paused=false;
		    
		    dinos = new ArrayList<Dino>();
		    for(int n = 0; n<NoD; n++) {
		    	dinos.add(new Dino());
		    }
		    

		    dino = new Dino();
		    score = 0;
		    gen = 0;
		    saving = false;
		    loading = false;
		}
		
	    public void nextGeneration() throws IOException
	    {
	        if(score > bestscore)
	        {
	            bestscore = score;
	            bestgen = gen;
	        }
	        gen++;
	       score = 0;        
	        
	        for(int n = dinos.size()-1; n >= 0; n--)
	            if(dinos.get(n).fitness < 0) dinos.remove(n);
	        
	        
	        boolean done = false;
	        while(!done)
	        {
	            done = true;
	            for(int n = 1; n < dinos.size(); n++)
	            {
	                if(dinos.get(n-1).fitness < dinos.get(n).fitness)
	                {
	                    Dino temp = dinos.remove(n-1);
	                    dinos.add(n, temp);
	                    done = false;
	                }
	            }
	        }
	        if(saving) dinos.get(0).SaveToFile();
	        
	        ArrayList<Dino> tempflock = new ArrayList<Dino>();
	        tempflock.add(Dino.CloneOf(dinos.get(0)));
	        tempflock.add(Dino.CloneOf(dinos.get(0)));
	        tempflock.add(Dino.CloneOf(dinos.get(0)));
	        
	        for(int n = 3; n < .9*NoD; n++)
	        {
	            int i = (int)(Math.random()*dinos.size());
	            tempflock.add(Dino.Combine(dinos.get(0),  dinos.get(i)));
	        }
	        
	        while(tempflock.size() < NoD)
	            tempflock.add(new Dino());
	                       
	        dinos = tempflock;
	        
	        cacti = new ArrayList<Cactus>();
	        cacti.add(new Cactus((int)(WIDE*.57)));
	        cacti.add(new Cactus(WIDE+10));
	        
	        if(loading) dinos.get(0).LoadFromFile();
	        saving = false;
	        loading = false;  //reset both to false 
	    }
		public void actionPerformed(ActionEvent e)
		{

			//if(!dino.alive)return; 
			if(!paused) {
				counter++;
				for(Cactus c : cacti) c.update();
				for(Dino d : dinos) d.update(cacti);
	
	    		
	    		if(cacti.get(cacti.size()-1).cactusx < .6*WIDE) 
	    			cacti.add(new Cactus(WIDE+10));
	    		
	    		if(cacti.get(0).cactusx < -5-CACWIDE)
	    			cacti.remove(0);
			}
    		int max = 0;
    		boolean done = true;
    		for(Dino d : dinos)
    		{
    			if(d.alive)
    			{
    				done = false;
    			
    				if(d.score > max)
    					max = d.score;
    			}    				
    		}
    		if(max > score) score = max;
    		
    		 if(done)  try {
    	         nextGeneration();
    	     }catch(IOException e1) {}
    		
    		
		//Once the new Visual() is launched, this method runs an infinite loop
		panel.repaint();
		}
		public void keyPressed(KeyEvent e)
		{
			if(e.getKeyCode() == KeyEvent.VK_HOME)
			Initialize();
			if(e.getKeyCode() == KeyEvent.VK_ESCAPE)
			System.exit(0);
		    if(e.getKeyCode() == KeyEvent.VK_L)
		        loading = true;
		    if(e.getKeyCode() == KeyEvent.VK_S)
		        saving = true; 
			if(e.getKeyCode()==KeyEvent.VK_SPACE) {
				if(paused) paused=!paused;
				if(dino.jumping) return;
				dino.jump();
			}
			if(e.getKeyCode() == KeyEvent.VK_ENTER) {
				paused=!paused;
			}
		}
		public void keyTyped(KeyEvent e) { } //not used
		public void keyReleased(KeyEvent e) { }//not used
		//BIG NOTE: The coordinate system for the output screen is as follows:
		// (x,y) = (0, 0) is the TOP LEFT corner of the output screen;
		// (x,y) = (WIDE, 0) is the TOP RIGHT corner of the output screen;
		// (x,y) = (0, HIGH) is the BOTTOM LEFT corner of the screen;
		// (x,y) = (WIDE, HIGH) is the BOTTOM RIGHT corner of the screen;
		//REMEMBER::
		// Strings are referenced from their BOTTOM LEFT corner.
		// Virtually all other objects (Rectangles, Ovals, Images...)
		// are referenced from their TOP LEFT corner.
			private class DrawingPanel extends JPanel implements Constants{
				public void paintComponent(Graphics g)
				{
					super.paintComponent(g);
					panel.setBackground(Color.black);
					//this is where you draw items on the screen.
					g.setColor(Color.WHITE);
					g.drawLine(0, HIGH/2, WIDE, HIGH/2);
//					ImageIcon Cactus = new ImageIcon("cactus.png");
//			        g.drawImage(Cactus.getImage(), WIDE/2, HIGH/2, 50, 50, null);
			        for(Cactus c : cacti) c.draw(g);
			        for(Dino d : dinos) d.draw(g);
			       // dino.draw(g);
//					ImageIcon Dino = new ImageIcon("dino.png");
//			        g.drawImage(Dino.getImage(), WIDE/2-40, HIGH/2-30, 40, 30, null);
			        
	            	if (paused) {
		            	g.setColor(Color.MAGENTA);
		            	
		            	g.setFont(LargeFont);
		            	FontMetrics fontvals = g.getFontMetrics();
		            	String output = "PAUSED";
		            	int outputwidth = fontvals.stringWidth(output);
		            	g.drawString(output, (WIDE-outputwidth)/2, HIGH/2+20);
		            	
		            	g.setFont(SmallFont);
		            	fontvals = g.getFontMetrics();
		            	output = "Hit <space> or <enter> to resume.";
		            	outputwidth = fontvals.stringWidth(output);
		            	g.drawString(output, (WIDE -outputwidth)/2, HIGH/2-50);
	            	
	            	}
	            	
	                g.setColor(Color.WHITE);
	                g.setFont(MediumFont);
	                g.drawString("Score = " + score, WIDE - 200, 50);
	                g.drawString("Gen # = " + gen, WIDE - 200, 90);
	                g.drawString("Best Gen = " + bestgen, WIDE - 200, 150);
	                g.drawString("Best Scr = " + bestscore, WIDE - 200, 190);
	            	
	                
	                g.setColor(Color.RED);
	                g.setFont(SmallFont);
	                if(saving) g.drawString("Saving Best Bird", WIDE - 250, HIGH - 40);
	                if(loading) g.drawString("Loading a Bird", WIDE - 250, HIGH - 10);
//	            	
				}
		}
}