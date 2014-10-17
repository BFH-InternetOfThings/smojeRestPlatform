package ch.bfh.smoje.entity;

public class CameraSensor implements ISensor {

	@Override
	public String getId() {
		return "Camera";
	}

	@Override
	public String getValue() {
		return "image data.....";
	}

	@Override
	public String getStatus() {
		return "OK";
	}

}
