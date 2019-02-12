package cubemover;

import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import java.io.OutputStream;
import java.util.Enumeration;

public class ServoMover {

	private CommPortIdentifier serialPortId;
	private Enumeration<?> enumComm;
	private SerialPort serialPort;
	private OutputStream outputStream;
	private int baudrate = 9600;
	private String portName = "COM8";

	public ServoMover() {

		if (openSerialPort(portName)) {
			System.out.println("Opened connection with " + portName);
		} else {
			System.out.println("Failed to open connection with " + portName);
		}

	}

	private boolean openSerialPort(String portName) {

		boolean foundPort = false;
		
		enumComm = CommPortIdentifier.getPortIdentifiers();
		
		while (enumComm.hasMoreElements()) {
			serialPortId = (CommPortIdentifier) enumComm.nextElement();
			
			if (portName.contentEquals(serialPortId.getName())) {
				foundPort = true;
				break;
			}
		}
		if (!foundPort)
			return false;

		try {
			serialPort = (SerialPort) serialPortId.open(this.getClass().getName(), 2000);
			outputStream = serialPort.getOutputStream();
			serialPort.setSerialPortParams(baudrate, SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
					SerialPort.PARITY_NONE);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}

	public void closePort() {
		serialPort.close();
	}

	public void led(boolean on) {
		try {
			outputStream.flush();
			outputStream.write((byte) 0xA7);
			if (on) {
				outputStream.write((byte) 0x00);
			} else {
				outputStream.write((byte) 0x01);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setSpeed(int channel, int speed) {

		String spd = Integer.toBinaryString(speed);
		String buf = "";

		for (int i = 0; i < 14 - (spd.length()); i++)
			buf += "0";
		spd = buf + spd;

		try {

			outputStream.flush();
			outputStream.write((byte) 0x87);
			outputStream.write((byte) channel);
			outputStream.write((byte) Integer.parseInt(spd.substring(7, 14), 2));
			outputStream.write((byte) Integer.parseInt(spd.substring(0, 7), 2));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void send(int channel, int position) {

		String pos = Integer.toBinaryString(position);
		String buf = "";
		for (int i = 0; i < 14 - (pos.length()); i++)
			buf += "0";
		pos = buf + pos;

		try {

			outputStream.flush();
			outputStream.write((byte) 0x84);
			outputStream.write((byte) channel);
			outputStream.write((byte) Integer.parseInt(pos.substring(7, 14), 2));
			outputStream.write((byte) Integer.parseInt(pos.substring(0, 7), 2));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
