package cubecontroller;

import java.io.Console;

import cubemover.CubeMover;
import cubemover.FaceTracker;
import cubescanner.CubeScanner;

public class CubeController {

	private WebcamViewer webcamViewer;
	private CubeMover cubeMover;
	private CubeScanner cubeScanner;

	public CubeController() {
		FaceTracker ft = new FaceTracker();
		webcamViewer = new WebcamViewer();
		cubeMover = new CubeMover(ft);
		cubeScanner = new CubeScanner(cubeMover, webcamViewer, ft);
		
		mainLoop();
	}

	public static void main(String args[]) {
		new CubeController();
	}

	private void mainLoop() {
		boolean exited = false;
		boolean scanned = false;

		Console console = System.console();
		if (console == null) {
			System.out.println("No console found. Non-interactive environment.");
			System.exit(0);
		}

		System.out.println("CubeController v1.0 | Liam O'Brien");

		while (!exited) {
			String input = console.readLine();

			if (input.equals("reset")) {
				cubeMover.reset();
				
			} else if (input.equals("scan")){
				if(cubeScanner.scanCube()) scanned = true;
				else System.out.println("Cube scan failed");
				
			} else if (input.equals("solve")){
				if(scanned) cubeMover.executeSequence(cubeScanner.getCubeSolveString());
				else System.out.println("Cube not yet scanned");
				
			} else if (input.equals("open")) {
				webcamViewer.open();
				
			} else if (input.equals("close")) {
				webcamViewer.close();
				
			} else if (input.equals("exit")){
				exited = true;
				
			} else {
				System.out.println("Unknown command");
			}
		}
		
	}

}
