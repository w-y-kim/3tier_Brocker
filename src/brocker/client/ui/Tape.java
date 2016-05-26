package brocker.client.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.JPanel;

import brocker.client.BrockerClient;
import brocker.vo.Stock;

public class Tape extends JPanel implements Runnable {

	private BrockerUI parent;
	private BrockerClient db;
	private String tapeString;
	private int x;
	private int tapeStringWidth;

	public Tape(BrockerUI parent, BrockerClient db) {
		this.parent = parent;
		this.db = db;
		
		this.setBackground(Color.GREEN);
		this.setPreferredSize(new Dimension(this.parent.tapeInsertOnFrame(), 25));
		//windowbuilder로 만들면 JFrame을 변수로 포함(composite)하기 때문에
		//UI에서 프레임을 상속받거나 프레임을 public으로 만들어줌 
		//최종버전에서는 메소드로 만들어서 변수에 접근하였음 
		try {
			getTapeString();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Font f = new Font("Tahoma", Font.PLAIN, 13);
		setFont(f);
		FontMetrics fm = getFontMetrics(f);//
		tapeStringWidth = fm.stringWidth(tapeString);

		Thread t = new Thread(this);
		t.start();

	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawString(tapeString, x, 18);
	}

	public void getTapeString() throws Exception {

		ArrayList<Stock> stockList;
		stockList = db.getAllStock();

		StringBuffer sb = new StringBuffer();
		for (Stock e : stockList) {
			sb.append(e.getSymbol() + " " + e.getPrice() + " | ");
		}
		tapeString = sb.toString();
	}

	@Override
	public void run() {
		while (true) {
			x++;
			repaint();
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
