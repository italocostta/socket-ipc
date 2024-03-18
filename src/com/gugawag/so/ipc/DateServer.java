package com.gugawag.so.ipc;

import java.io.*;
import java.net.*;
import java.util.Date;

public class DateServer {
	public static void main(String[] args) {
		try {
			ServerSocket sock = new ServerSocket(6013);

			System.out.println("=== Servidor iniciado ===\n");
			// escutando por conexões
			while (true) {
				Socket client = sock.accept();
				// Se chegou aqui, foi porque algum cliente se comunicou
				System.out.println("Servidor recebeu comunicação do ip: " + client.getInetAddress() + "-" + client.getPort());
				// Cria e inicia uma nova thread para lidar com o cliente
				ClientHandler handler = new ClientHandler(client);
				handler.start();
			}
		} catch (IOException ioe) {
			System.err.println(ioe);
		}
	}

	// Classe interna para manipular a lógica do cliente em uma thread separada
	private static class ClientHandler extends Thread {
		private Socket clientSocket;

		public ClientHandler(Socket socket) {
			this.clientSocket = socket;
		}

		public void run() {
			try {
				PrintWriter pout = new PrintWriter(clientSocket.getOutputStream(), true);

				// Escreve a data atual no socket
				pout.println(new Date().toString() + "-Boa noite alunos!");

				InputStream in = clientSocket.getInputStream();
				BufferedReader bin = new BufferedReader(new InputStreamReader(in));

				String line;
				while ((line = bin.readLine()) != null) {
					System.out.println("O cliente me disse:" + line);
				}

				// fechar o socket
				clientSocket.close();
			} catch (IOException e) {
				System.err.println(e);
			}
		}
	}
}
