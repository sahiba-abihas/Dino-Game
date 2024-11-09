import java.awt.Graphics;

import javax.swing.ImageIcon;

public class Cactus implements Constants {
	public int cactusx;
	public int cactusy;
	public boolean scored;
	
	public Cactus(int x) {
		scored = false;
		cactusx = x;
		cactusy = 310;
	}
	
	public void update() {
		cactusx-= CACSPEED;

	}
	
	public void draw(Graphics g) {
		ImageIcon Cactus = new ImageIcon("cactus.png");
		g.drawImage(Cactus.getImage(), cactusx, cactusy, CACWIDE, CACHIGH, null);
		//g.fillRect(cactusx, 310, CACWIDE, CACHIGH);
	}
}
