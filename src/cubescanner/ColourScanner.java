package cubescanner;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import cubecontroller.WebcamViewer;

import utils.Rectangle;

public class ColourScanner {
	
	private final int RGB_TRIALS = 250;

	private ArrayList<Color> UFaceData, FFaceData, LFaceData, RFaceData, BFaceData, DFaceData;
	
	private Color UIdentity, FIdentity, LIdentity, RIdentity, BIdentity, DIdentity;
	
	private Random random;
	private WebcamViewer webcamViewer;
	
	public ColourScanner(WebcamViewer webcamViewer){
		this.webcamViewer = webcamViewer;
		this.random = new Random();
		
		UFaceData = new ArrayList<Color>();
		FFaceData = new ArrayList<Color>();
		LFaceData = new ArrayList<Color>();
		RFaceData = new ArrayList<Color>();
		BFaceData = new ArrayList<Color>();
		DFaceData = new ArrayList<Color>();
	}
	
	public void scan(BufferedImage webcamImage, char face){
		for(int i = 0; i < webcamViewer.scanAreas.size(); i++){
			Rectangle scanArea = webcamViewer.scanAreas.get(i);
			
			int redTotal = 0;
			int greenTotal = 0;
			int blueTotal = 0;
			
			for(int a = 0; a < RGB_TRIALS; a++){
				int x = random.nextInt(scanArea.getWidth()) + scanArea.getX();
				int y = random.nextInt(scanArea.getHeight()) + scanArea.getY();
				
				Color clr = new Color(webcamImage.getRGB(x, y));
				
				redTotal += clr.getRed();
				greenTotal += clr.getGreen();
				blueTotal += clr.getBlue();
			}
			
			Color color = new Color(redTotal / RGB_TRIALS, greenTotal / RGB_TRIALS, blueTotal / RGB_TRIALS);
			
			if(face == 'U') UFaceData.add(color);
			else if(face == 'F') FFaceData.add(color);
			else if(face == 'D') DFaceData.add(color);
			else if(face == 'B') BFaceData.add(color);
			else if(face == 'L') LFaceData.add(color);
			else if(face == 'R') RFaceData.add(color);
		}
		
	}
	
	public String determineCubeState(){
		ArrayList<Character> cubeStateArray = new ArrayList<Character>();
		
		int[] SideFaceOrder = {6, 3, 0, 7, 4, 1, 8, 5, 2};
		
		UIdentity = UFaceData.get(4);
		FIdentity = FFaceData.get(4);
		LIdentity = LFaceData.get(4);
		RIdentity = RFaceData.get(4);
		BIdentity = BFaceData.get(4);
		DIdentity = DFaceData.get(4);
		
		for(int i = 8; i >= 0; i--){
			cubeStateArray.add(getIdentity(UFaceData.get(i)));
		}
		for(int i : SideFaceOrder){
			cubeStateArray.add(getIdentity(RFaceData.get(i)));
		}
		for(int i = 8; i >= 0; i--){
			cubeStateArray.add(getIdentity(FFaceData.get(i)));
		}
		for(int i = 8; i >= 0; i--){
			cubeStateArray.add(getIdentity(DFaceData.get(i)));
		}
		for(int i : SideFaceOrder){
			cubeStateArray.add(getIdentity(LFaceData.get(i)));
		}
		for(int i = 0; i < 9; i++){
			cubeStateArray.add(getIdentity(BFaceData.get(i)));
		}
		
		String cubeStateString = getString(cubeStateArray);
		return cubeStateString;
	}
	
	
	private char getIdentity(Color clr){
		double USimilarityScore = (Math.abs(UIdentity.getRed() - clr.getRed()) +
								Math.abs(UIdentity.getGreen() - clr.getGreen()) +
								Math.abs(UIdentity.getBlue() - clr.getBlue())) / 3;
		
		double FSimilarityScore = (Math.abs(FIdentity.getRed() - clr.getRed()) +
								Math.abs(FIdentity.getGreen() - clr.getGreen()) +
								Math.abs(FIdentity.getBlue() - clr.getBlue())) / 3;
		
		double LSimilarityScore = (Math.abs(LIdentity.getRed() - clr.getRed()) +
								Math.abs(LIdentity.getGreen() - clr.getGreen()) +
								Math.abs(LIdentity.getBlue() - clr.getBlue())) / 3;
		
		double RSimilarityScore = (Math.abs(RIdentity.getRed() - clr.getRed()) +
								Math.abs(RIdentity.getGreen() - clr.getGreen()) +
								Math.abs(RIdentity.getBlue() - clr.getBlue())) / 3;
		
		double BSimilarityScore = (Math.abs(BIdentity.getRed() - clr.getRed()) +
								Math.abs(BIdentity.getGreen() - clr.getGreen()) +
								Math.abs(BIdentity.getBlue() - clr.getBlue())) / 3;
		
		double DSimilarityScore = (Math.abs(DIdentity.getRed() - clr.getRed()) +
								Math.abs(DIdentity.getGreen() - clr.getGreen()) +
								Math.abs(DIdentity.getBlue() - clr.getBlue())) / 3;
		
		double[] similarityArray = {USimilarityScore, FSimilarityScore, LSimilarityScore, RSimilarityScore, BSimilarityScore, DSimilarityScore};	
		Arrays.sort(similarityArray);
		
		if(similarityArray[0] == USimilarityScore) return 'U';
		else if(similarityArray[0] == FSimilarityScore) return 'F';
		else if(similarityArray[0] == LSimilarityScore) return 'L';
		else if(similarityArray[0] == RSimilarityScore) return 'R';
		else if(similarityArray[0] == BSimilarityScore) return 'B';
		else if(similarityArray[0] == DSimilarityScore) return 'D';
		else return 'Z';
	}
	
	private String getString(ArrayList<Character> list)
	{    
	    StringBuilder builder = new StringBuilder(list.size());
	    for(Character ch: list)
	    {
	        builder.append(ch);
	    }
	    return builder.toString();
	}
	
}
