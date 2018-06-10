package main;

import java.io.IOException;
import java.net.*;
import java.util.*;

public class Process implements Runnable {
	private int port, mult;
	private InetAddress address;
	private MulticastSocket socket;
	private long time, id;
	private boolean inCritical;
	private int nProcess;
	private ArrayList<Message> queue;
	
	public Process(String host, int port, int mult, long count, int nProcess) throws IOException {
		socket = new MulticastSocket(port);
		address = InetAddress.getByName(host);
		this.port = port;
		this.mult = mult;
		this.time = count;
		this.inCritical = false;
		this.nProcess = nProcess;
		socket.joinGroup(address);
		this.queue = new ArrayList<Message>();
	}

	public synchronized void sairRegiaoCritica() {
		for (int i = 0 ; i < this.queue.size() ; i++) {
			try {
				Message message = this.queue.get(i);
				System.out.println(System.currentTimeMillis() + " " + this.id + " habilitando message de " + message.getPID());
				message.enableResponse(this.id);
				socket.send(new DatagramPacket(message.getBytes(), message.getBytes().length, address, port));
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		this.inCritical = false;
		this.queue = new ArrayList<Message>();
	}

	public void run() {
		this.id = Thread.currentThread().getId();
		System.out.println("Criando processo: " + this.id + " " + this.mult);
		Message msg = null;
		ArrayList<Message> responses = null;

		try {
			for (int i = 1; true; i++) {
				if ((i % this.mult) == 0) {
					msg = new Message("Alo " + i, this.id, time++);
					System.out.println(System.currentTimeMillis() + " " + this.id + " solicitando " + msg);
					byte[] msgB = msg.getBytes();
					responses = new ArrayList<Message>();
					socket.send(new DatagramPacket(msgB, msgB.length, address, port));
				}

				byte[] buff = new byte[4096];
				DatagramPacket receive = new DatagramPacket(buff, buff.length);
				socket.setSoTimeout(200);

				try {
					socket.receive(receive);
				} catch (Exception ex) {
					continue;
				}
				Message message = new Message(receive.getData());
				System.out.println(System.currentTimeMillis() + " " + this.id + " recebeu " + message);
				if (message.getPID() != this.id) {
					if (message.getPIDResponse() == 0) {
						time = (int) Math.max(time, message.getCount()) + 1;
						if (!this.inCritical && responses == null) {
							System.out.println(System.currentTimeMillis() + " " + this.id + " habilitando message de " + message.getPID());
							message.enableResponse(this.id);
							socket.send(new DatagramPacket(message.getBytes(), message.getBytes().length, address, port));
						} else if (this.inCritical) {
							System.out.println(System.currentTimeMillis() + " " + this.id + " adicionando na queue " + message);
							this.queue.add(message);
						} else {
							if (msg.getCount() > message.getCount()) {
								System.out.println(System.currentTimeMillis() + " " + this.id + " habilitando message de " + message.getPID());
								message.enableResponse(this.id);
								socket.send(new DatagramPacket(message.getBytes(), message.getBytes().length, address, port));
							} else {
								System.out.println(System.currentTimeMillis() + " " + this.id + " adicionando na queue " + message);
								this.queue.add(message);
							}
						}
					}
				} else {
					if (message.getPIDResponse() != 0) {
						if (responses != null) {							
							responses.add(message);
							if (responses.size() == this.nProcess - 1 && !this.inCritical) {
								responses = null;
								this.inCritical = true;
								new Thread(new CriticalRegion(this, 2)).start();
							} 
						}
					}
				}
			}
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
}