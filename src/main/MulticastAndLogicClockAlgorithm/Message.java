package main;

import java.io.*;

public class Message implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private long PID;
	private long PIDResponse;
	private String name;
	private long count;
	
	public Message(byte[] params) throws IOException, ClassNotFoundException {
		ByteArrayInputStream bis = new ByteArrayInputStream(params);
		ObjectInput in = new ObjectInputStream(bis);

		Message newMessage = (Message) in.readObject();
		this.PID = newMessage.PID;
		this.name = newMessage.name;
		this.count = newMessage.count;
		this.PIDResponse = newMessage.PIDResponse;
	}
	
	public Message(Message message) {
		this.PID = message.PID;
		this.name = message.name;
		this.PIDResponse = message.PIDResponse;
		this.count = message.count;
	}
	
	public Message (String name, long PID, long count) {
		this.name = name;
		this.PID = PID;
		this.count = count;
		this.PIDResponse = 0;
	}
	
	public String getMessage() {
		return this.name;
	}
	
	public long getPID() {
		return this.PID;
	}
	
	public long getCount() {
		return this.count;
	}
	
	public byte[] getBytes() throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutput out = new ObjectOutputStream(bos);   
		out.writeObject(this);
		out.flush();
		bos.close();
		return bos.toByteArray();
	}
	
	public void enableResponse(long pid) {
		this.PIDResponse = pid;
	}

	public long getPIDResponse () {
		return this.PIDResponse;
	}

	public String toString() {
		return "Message: nome - " + this.name + " - pid - " + this.PID + " - count - " + this.count + " - resposta: " + this.PIDResponse;
	}
}
