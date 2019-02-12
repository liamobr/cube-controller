package cubemover;

public class CubeMover {

	private final int SERVO_0_RETRACT_POS = 1888 * 4;
	private final int SERVO_0_EXTEND_POS = 1280 * 4;
	private final int SERVO_0_HOLD_POS = 1610 * 4;

	private final int SERVO_1_TURN_POS_0 = 2480 * 4;
	private final int SERVO_1_TURN_POS_1 = 1500 * 4;
	private final int SERVO_1_TURN_POS_2 = 528 * 4;

	private int servo1Pos;

	private ServoMover servoMover;
	private FaceTracker ft;

	public CubeMover(FaceTracker ft) {

		servoMover = new ServoMover();
		this.ft = ft;
		
		rotatePos1();
		servo1Pos = 1;
		servoMover.setSpeed(0, 90);
	}
	
	public void reset(){
		rotatePos1();
		retractArm();
	}

	public void executeSequence(String sequence) {

		boolean inverted;
		boolean twice;

		String[] sequenceArray = sequence.split(" ");

		for (int c = 0; c < sequenceArray.length; c++) {
			sequenceArray[c] = sequenceArray[c].trim();
		}

		for (int i = 0; i < sequenceArray.length; i++) {
			if (sequenceArray[i].length() > 1) {
				if (sequenceArray[i].charAt(1) == '2') {
					twice = true;
					inverted = false;
				} else {
					twice = false;

					if (sequenceArray[i].charAt(1) == '\'') {
						inverted = true;
					} else {
						inverted = false;
					}
				}
			} else {
				inverted = twice = false;
			}

			if (sequenceArray[i].charAt(0) == 'R') {
				executeMove('R', inverted, ft, true);
				if (twice)
					executeMove('R', inverted, ft, true);

			} else if (sequenceArray[i].charAt(0) == 'L') {
				executeMove('L', inverted, ft, true);
				if (twice)
					executeMove('L', inverted, ft, true);

			} else if (sequenceArray[i].charAt(0) == 'U') {
				executeMove('U', inverted, ft, true);
				if (twice)
					executeMove('U', inverted, ft, true);

			} else if (sequenceArray[i].charAt(0) == 'D') {
				executeMove('D', inverted, ft, true);
				if (twice)
					executeMove('D', inverted, ft, true);

			} else if (sequenceArray[i].charAt(0) == 'F') {
				executeMove('F', inverted, ft, true);
				if (twice)
					executeMove('F', inverted, ft, true);

			} else if (sequenceArray[i].charAt(0) == 'B') {
				executeMove('B', inverted, ft, true);
				if (twice)
					executeMove('B', inverted, ft, true);
			}

		}

	}
	

	public void executeMove(char face, boolean inverted, FaceTracker ft, boolean turn) {

		if (ft.findLocationOf(face) == 'R') {
			try {
				if (servo1Pos == 1) {
					rotatePos0();
					ft.rotateYi();
					Thread.sleep(1000);
					pushOver(ft);
				} else if (servo1Pos == 2) {
					rotatePos1();
					ft.rotateYi();
					Thread.sleep(1000);
					pushOver(ft);
				} else {
					rotatePos1();
					ft.rotateY();
					Thread.sleep(1000);
					pushOver(ft);
					pushOver(ft);
					pushOver(ft);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		} else if (ft.findLocationOf(face) == 'L') {
			try {
				if (servo1Pos == 1) {
					rotatePos2();
					ft.rotateY();
					Thread.sleep(1000);
					pushOver(ft);
				} else if (servo1Pos == 2) {
					rotatePos1();
					ft.rotateYi();
					Thread.sleep(1000);
					pushOver(ft);
					pushOver(ft);
					pushOver(ft);
				} else {
					rotatePos1();
					ft.rotateY();
					Thread.sleep(1000);
					pushOver(ft);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		} else if (ft.findLocationOf(face) == 'U') {
			pushOver(ft);
			pushOver(ft);

		} else if (ft.findLocationOf(face) == 'F') {
			pushOver(ft);
			pushOver(ft);
			pushOver(ft);

		} else if (ft.findLocationOf(face) == 'B') {
			pushOver(ft);
		}
		
		if(turn){
			if (inverted) {
				turnLeft(ft);
			} else {
				turnRight(ft);
			}
		}
		
		try{
			Thread.sleep(1000);
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}

	private void pushOver(FaceTracker ft) {
		try {
			extendArmFull();
			Thread.sleep(800);
			retractArm();
			ft.rotateX();
			Thread.sleep(800);

		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void turnRight(FaceTracker ft) {
		try {
			if (servo1Pos == 1) {
				extendArmHold();
				Thread.sleep(800);
				rotatePos0();
				Thread.sleep(1000);
				retractArm();
				Thread.sleep(800);
			} else if (servo1Pos == 2) {
				extendArmHold();
				Thread.sleep(800);
				rotatePos1();
				Thread.sleep(1000);
				retractArm();
				Thread.sleep(800);
			} else {
				rotatePos1();
				ft.rotateY();
				Thread.sleep(1000);
				extendArmHold();
				Thread.sleep(800);
				rotatePos0();
				Thread.sleep(1000);
				retractArm();
				Thread.sleep(800);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	private void turnLeft(FaceTracker ft) {
		try {
			if (servo1Pos == 1) {
				extendArmHold();
				Thread.sleep(800);
				rotatePos2();
				Thread.sleep(1000);
				retractArm();
				Thread.sleep(800);
			} else if (servo1Pos == 0) {
				extendArmHold();
				Thread.sleep(800);
				rotatePos1();
				Thread.sleep(1000);
				retractArm();
				Thread.sleep(800);
			} else {
				rotatePos1();
				ft.rotateYi();
				Thread.sleep(1000);
				extendArmHold();
				Thread.sleep(800);
				rotatePos2();
				Thread.sleep(1000);
				retractArm();
				Thread.sleep(800);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	private void rotatePos0() {
		servo1Pos = 0;
		servoMover.send(1, SERVO_1_TURN_POS_0);
	}

	private void rotatePos1() {
		servo1Pos = 1;
		servoMover.send(1, SERVO_1_TURN_POS_1);
	}

	public void rotatePos2() {
		servo1Pos = 2;
		servoMover.send(1, SERVO_1_TURN_POS_2);
	}

	private void extendArmFull() {
		servoMover.send(0, SERVO_0_EXTEND_POS);
	}

	private void extendArmHold() {
		servoMover.send(0, SERVO_0_HOLD_POS);
	}

	private void retractArm() {
		servoMover.send(0, SERVO_0_RETRACT_POS);
	}

}

