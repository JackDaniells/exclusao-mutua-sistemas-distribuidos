package main;

import java.io.*;
import java.util.*;
import java.net.*;

public class MulticastAndLogicClockAlgorithm {
	public MulticastAndLogicClockAlgorithm(int n) {
		int port = 5001;
		String host = "224.0.0.12";
		Random rand = new Random();
		ArrayList<Process> processos = new ArrayList<Process>();
		
		try {
			for (int i = 1 ; i <= n ; i++)
				processos.add(new Process(host, port, (rand.nextInt(100) + 30), i, n));
			for (Process processo : processos)
				new Thread(processo).start();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
}