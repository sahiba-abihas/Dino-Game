import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import javax.swing.ImageIcon;

public class Dino implements Constants {
    public int posx;
    public int posy;
    public double vely;
    public boolean alive;
    public boolean jumping;
    public int score;
    public NeuralNetwork brain;
    public double fitness;
    public float sec;
    public int jumpcountconsec;
    public int jumpCooldownCounter;

    public Dino() {
        posx = 400;
        posy = 380;
        vely = 0;
        alive = true;
        jumping = false;
        score = 0;
        brain = new NeuralNetwork(5, 9, 3, 1);
        fitness = 0.0;
        sec = 0;
        jumpcountconsec = 0;
        jumpCooldownCounter = 0;
    }

    public void update(ArrayList<Cactus> cacti) {
        if (!alive) return;

        fitness += 0.1;

        // Check collision with obstacles
        for (Cactus cactus : cacti) {
            if (cactus.cactusx + CACWIDE < (posx - DINOWIDE) && posy == 380) {
                if (!cactus.scored) {
                    cactus.scored = true;
                    fitness += 2;
                    score++;
                    System.out.println(score);
                }
            }

            Rectangle dino = new Rectangle(posx - 50, posy - 50, DINOWIDE, DINOHIGH);
            Rectangle obstacle = new Rectangle(cactus.cactusx, 310, CACWIDE, CACHIGH);
            if (dino.intersects(obstacle)) {
                alive = false;
            }
        }

       
        int current = 0;
        double[] senses = new double[5];
        senses[0] = (double) posx / WIDE;
        senses[1] = (double) posy / HIGH;
        senses[2] = (double) vely / MAXVEL;
        senses[3] = (double) cacti.get(current).cactusx / WIDE;
        senses[4] = (double) cacti.get(current + 1).cactusx / WIDE;

        double[] thoughts = brain.think(senses);
        if (thoughts[0] > 0 && jumpCooldownCounter <= 0) {
            jump();
            jumpCooldownCounter = 16; 
        } else {
            jumpCooldownCounter--;
        }

        // Apply gravity
        if (posy < 380) {
            vely += gravity;
            if (Math.abs(vely) > MAXVEL) {
                vely = vely / Math.abs(vely) * MAXVEL;
            }
            posy += vely;
        }



    }

    public void jump() {
        if (jumping) {
            return;
        }

        jumping = true;
        posy-=2;
        vely = -1*JUMP;
    }
	
    public static Dino CloneOf(Dino d)
    {
        Dino temp = new Dino();   
        temp.brain.copy(d.brain);
        temp.brain.mutate();
        
        return temp;
    }
    public static Dino Combine(Dino a, Dino b)
    {
        Dino temp = new Dino();
        temp.brain.combineBrains(a.brain, b.brain);
        temp.brain.mutate();
        
        return temp;
    }
    public void SaveToFile() throws IOException
  {
        FileWriter outfile = new FileWriter("SavedBrain.txt");
        BufferedWriter fout = new BufferedWriter(outfile);
        
        fout.write(""+brain.numinputs);
        fout.newLine();
        fout.write(""+brain.numhidden1);
        fout.newLine();
        fout.write(""+brain.numhidden2);
        fout.newLine();
        fout.write(""+brain.numoutputs);
        fout.newLine();
        for(int r = 0; r < brain.connectIH1.rows; r++)
            for(int c = 0; c < brain.connectIH1.cols; c++)
            {
                fout.write(""+brain.connectIH1.elements[r][c]);
                fout.newLine();
            }
        for(int r = 0; r < brain.connectH1H2.rows; r++)
            for(int c = 0; c < brain.connectH1H2.cols; c++)
            {
                fout.write(""+brain.connectH1H2.elements[r][c]);
                fout.newLine();
            }
        for(int r = 0; r < brain.connectH2O.rows; r++)
            for(int c = 0; c < brain.connectH2O.cols; c++)
            {
                fout.write(""+brain.connectH2O.elements[r][c]);
                fout.newLine();
            }
        for(int r = 0; r < brain.biasH1.rows; r++)
            for(int c = 0; c < brain.biasH1.cols; c++)
            {
                fout.write(""+brain.biasH1.elements[r][c]);
                fout.newLine();
            }
        for(int r = 0; r < brain.biasH2.rows; r++)
            for(int c = 0; c < brain.biasH2.cols; c++)
            {
                fout.write(""+brain.biasH2.elements[r][c]);
                fout.newLine();
            }
        for(int r = 0; r < brain.biasO.rows; r++)
            for(int c = 0; c < brain.biasO.cols; c++)
            {
                fout.write(""+brain.biasO.elements[r][c]);
                fout.newLine();
            }
        fout.close();
    }


    public static Dino LoadFromFile() throws FileNotFoundException
    {
        FileReader inputfile = new FileReader("SavedBrain.txt");
        Scanner fin = new Scanner(inputfile);
        
        Dino ret = new Dino();
        
        int i, h1, h2, o;
        i = fin.nextInt();
        h1 = fin.nextInt();
        h2 = fin.nextInt();
        o = fin.nextInt();
        
        if(i != ret.brain.numinputs || h1 != ret.brain.numhidden1 || h2 != ret.brain.numhidden2 || o != ret.brain.numoutputs)
        {
            System.out.println("ERROR IN LOADING -- THE SAVE BRAIN DOES NOT FIT!!!!");
            return ret;
        }
        
        for(int r = 0; r < ret.brain.connectIH1.rows; r++)
            for(int c = 0; c < ret.brain.connectIH1.cols; c++)
                ret.brain.connectIH1.elements[r][c] = fin.nextDouble();
                
        for(int r = 0; r < ret.brain.connectH1H2.rows; r++)
            for(int c = 0; c < ret.brain.connectH1H2.cols; c++)
                ret.brain.connectH1H2.elements[r][c] = fin.nextDouble();
                
        for(int r = 0; r < ret.brain.connectH2O.rows; r++)
            for(int c = 0; c < ret.brain.connectH2O.cols; c++)
                ret.brain.connectH2O.elements[r][c] = fin.nextDouble();
                
        for(int r = 0; r < ret.brain.biasH1.rows; r++)
            for(int c = 0; c < ret.brain.biasH1.cols; c++)
                ret.brain.biasH1.elements[r][c] = fin.nextDouble();
                
        for(int r = 0; r < ret.brain.biasH2.rows; r++)
            for(int c = 0; c < ret.brain.biasH2.cols; c++)
                ret.brain.biasH2.elements[r][c] = fin.nextDouble();
                
        for(int r = 0; r < ret.brain.biasO.rows; r++)
            for(int c = 0; c < ret.brain.biasO.cols; c++)
                ret.brain.biasO.elements[r][c] = fin.nextDouble();
                
        return ret;
    }
    
	
	public void draw(Graphics g) {
		if(!alive) return;
		if(jumping) {
			g.setColor(Color.WHITE); 
			
			
		}
		if(posy>=380) jumping = false;
		
		else g.setColor(Color.YELLOW);
		g.fillOval(posx-50, posy-50, DINOWIDE, DINOHIGH);
		
		//g.fillRect(posx-50, posy-50, DINOWIDE, DINOHIGH);
//		ImageIcon Dino = new ImageIcon("dino.png");
//		g.drawImage(Dino.getImage(), posx-50, posy-50, DINOWIDE, DINOHIGH, null);
//		
	}
	
	
}
