package ch.bfh.smoje.entity;



public class CameraSensor implements ISensor {

	@Override
	public String getId() {
		return "Camera";
	}

	@Override
	public String getValue() {
		
		//TODO: LED Steuerung http://pi4j.com/usage.html
		
		return "image data.....";
	}

	@Override
	public String getStatus() {
		return "OK";
	}

}
