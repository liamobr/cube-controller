package cubescanner;

import org.kociemba.twophase.*;

import cubecontroller.WebcamViewer;
import cubemover.CubeMover;
import cubemover.FaceTracker;

public class CubeScanner {
	
	private CubeMover cubeMover;
	private FaceTracker faceTracker;
	private WebcamViewer webcamViewer;
	private ColourScanner colourScanner;

	private String cubeStateString = ""; //BLUUUUBRDBFFLRLLULRBLLFBUFDFRFDDRDDUDBUDLDRFLRURFBBBRF
	private String cubeSolveString = "";
	
	public CubeScanner(CubeMover cubeMover, WebcamViewer webcamViewer, FaceTracker ft){
		this.cubeMover = cubeMover;
		this.webcamViewer = webcamViewer;
		this.colourScanner = new ColourScanner(webcamViewer);
		
		this.faceTracker = ft;
	}
	
	public boolean scanCube(){
		cubeMover.executeMove('D', false, faceTracker, false);
		colourScanner.scan(webcamViewer.getImage(), 'U');
		
		cubeMover.executeMove('B', false, faceTracker, false);
		colourScanner.scan(webcamViewer.getImage(), 'F');
		
		cubeMover.executeMove('U', false, faceTracker, false);
		colourScanner.scan(webcamViewer.getImage(), 'D');
		
		cubeMover.executeMove('F', false, faceTracker, false);
		colourScanner.scan(webcamViewer.getImage(), 'B');
		
		cubeMover.executeMove('R', false, faceTracker, false);
		colourScanner.scan(webcamViewer.getImage(), 'L');
		
		cubeMover.executeMove('L', false, faceTracker, false);
		colourScanner.scan(webcamViewer.getImage(), 'R');
		
		cubeMover.executeMove('D', false, faceTracker, false);
		
		cubeStateString = colourScanner.determineCubeState();
		System.out.println(cubeStateString);
		cubeSolveString = Search.solution(cubeStateString, 24, 5, false);
		System.out.println(cubeSolveString);
		
		return true;
	}
	
	public String getCubeSolveString(){
		return cubeSolveString;
	}
	
}
