package cubecontroller;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

import org.imgscalr.Scalr;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamResolution;

import utils.Rectangle;

public class WebcamViewer implements ActionListener {
	
	public ArrayList<Rectangle> scanAreas;

	private final int TIMER_INTERVAL = 100;
	private final Dimension PREFERRED_SIZE = new Dimension(350, 280);
	private Dimension[] nonStandardResolutions = new Dimension[] {
			WebcamResolution.HD720.getSize()
	};
	
	private Webcam webcam;
	private JFrame frame;
	private JPanel panel;
	private Timer timer;
	private double imgScale;
	private boolean open;

	public WebcamViewer() {
		panel = new JPanel(){
			private static final long serialVersionUID = 1L;

			@Override
			public void paintComponent(Graphics g){
				super.paintComponent(g);
				g.setColor(Color.YELLOW);
				
				updateFrame(g);
				
				for(int i = 0; i < scanAreas.size(); i++){
					Rectangle rect = scanAreas.get(i);
					
					g.drawLine( (int) (rect.getX() * imgScale), (int) (rect.getY() * imgScale), 
							(int) ((rect.getX() + rect.getWidth()) * imgScale), (int) (rect.getY() * imgScale));
					g.drawLine( (int) (rect.getX() * imgScale), (int) (rect.getY() * imgScale) , 
							(int) (rect.getX() * imgScale), (int) ((rect.getY() + rect.getHeight()) * imgScale));
					g.drawLine( (int) (rect.getX() * imgScale), (int) ((rect.getY() + rect.getHeight()) * imgScale), 
							(int) ((rect.getX() + rect.getWidth()) * imgScale), (int) ((rect.getY() + rect.getHeight()) * imgScale));
					g.drawLine( (int) ((rect.getX() + rect.getWidth()) * imgScale), (int) (rect.getY() * imgScale), 
							(int) ((rect.getX() + rect.getWidth()) * imgScale), (int) ((rect.getY() + rect.getHeight()) * imgScale));
				}
			}
		};
		
		scanAreas = new ArrayList<Rectangle>();
		scanAreas.add(new Rectangle(370, 120, 80, 80));
		scanAreas.add(new Rectangle(570, 120, 80, 80));
		scanAreas.add(new Rectangle(770, 120, 80, 80));
		scanAreas.add(new Rectangle(350, 270, 90, 90));
		scanAreas.add(new Rectangle(565, 270, 90, 90));
		scanAreas.add(new Rectangle(780, 270, 90, 90));
		scanAreas.add(new Rectangle(320, 455, 100, 100));
		scanAreas.add(new Rectangle(560, 455, 100, 100));
		scanAreas.add(new Rectangle(800, 455, 100, 100));
		
		open = false;
		webcam = Webcam.getDefault();
		webcam.setCustomViewSizes(nonStandardResolutions);
		webcam.setViewSize(WebcamResolution.HD720.getSize());

		if (webcam.open()) {
			System.out.println("Webcam open");
		} else {
			System.out.println("Webcam failed to open");
		}
	}

	public void open() {
		if (!open) {
			frame = new JFrame();
			frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
			frame.setPreferredSize(PREFERRED_SIZE);
			frame.setResizable(true);
			frame.add(panel);
			frame.pack();
			frame.setVisible(true);
			open = !open;

			timer = new Timer(TIMER_INTERVAL, this);
			timer.start();
		} else {
			System.out.println("Window is already open.");
		}
	}

	public void close() {
		if (open) {
			frame.setVisible(false);
			frame.remove(panel);
			frame.dispose();
			open = !open;

			timer.stop();
		} else {
			System.out.println("Window is already closed.");
		}

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		panel.repaint();
	}
	
	public BufferedImage getImage(){
		return webcam.getImage();
	}

	private void updateFrame(Graphics g) {
		BufferedImage src = webcam.getImage();
		BufferedImage image = Scalr.resize(src, frame.getContentPane().getWidth());
		
		imgScale = (double) image.getWidth() / (double) src.getWidth();
		
		g.drawImage(image, 0, 0, null);
	}
	

}
