package cubemover;

public class FaceTracker {
	
	private char upFace, downFace, frontFace, backFace, leftFace, rightFace;
	
	public FaceTracker(){
		upFace = 'U';
		downFace = 'D';
		frontFace = 'F';
		backFace = 'B';
		leftFace = 'L';
		rightFace = 'R';
	}
	
	public char findLocationOf(char side){
		if(upFace == side){
			return 'U';
		}else if(downFace == side){
			return 'D';
		}else if(frontFace == side){
			return 'F';
		}else if(backFace == side){
			return 'B';
		}else if(leftFace == side){
			return 'L';
		}else{
			return 'R';
		}
	}
	
	public void rotateX(){
		char holder = upFace;
		
		upFace = frontFace;
		frontFace = downFace;
		downFace = backFace;
		backFace = holder;
		
	}
	
	public void rotateXi(){
		char holder = upFace;
		
		upFace = backFace;
		backFace = downFace;
		downFace = frontFace;
		frontFace = holder;
	}
	
	public void rotateYi(){
		char holder = frontFace;
		
		frontFace = leftFace;
		leftFace = backFace;
		backFace = rightFace;
		rightFace = holder;
	}
	
	public void rotateY(){
		char holder = frontFace;
		
		frontFace = rightFace;
		rightFace = backFace;
		backFace = leftFace;
		leftFace = holder;
	}
}

