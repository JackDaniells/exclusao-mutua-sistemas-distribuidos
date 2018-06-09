package main;

import java.util.LinkedList;


public class PermissionBasedAlgorithm {
	//tamanho da fila
	static int queueSize;    
    //fila de itens
    static LinkedList<MyNode> queue = new LinkedList<MyNode>();    
    //fila de proximo
    static int next[];
    //contador de acessos a regi�o cr�tica
    static int criticalAreaCounter = 0;
    
    //construtor
    /**********************************************************/
    PermissionBasedAlgorithm(int size) { 
    	
    	this.queueSize = size;
    	 next = new int[queueSize];
    	
    	//p�e os nodos na fila
        for (int i = 0; i < queueSize; i++) {
        	//cria o nodo
            MyNode node = new MyNode(i, false);
            //inicia o nodo
            node.start();
            //adiciona o nodo na fila
            queue.add(node);
            //define o ultimo elemento apontando pro primeiro
            if(i == queueSize - 1) next[i] = 0;
            else next[i] = i + 1;   
        }

        
        //inicia o token no primeiro nodo
        MyNode first = queue.element();
        first.receiveToken(true);
     }  
    
    
    //representa��o da regi�o cr�tica
    /**********************************************************/
    static void criticalArea(MyNode n) throws Exception {
    	if(n.hasToken) {
    		//criticalAreaCounter++;
    		//System.out.println("=====");
    		//System.out.println("Acessos simult�neos a �rea cr�tica: " + criticalAreaCounter);
    		//System.out.println("=====");
    		Thread.sleep(5000);
    		//criticalAreaCounter--;
    	} else {
    		System.err.println("Erro de Acesso");
    		throw new Exception("Acesso indevido a regi�o cr�tica");
    	}
    	
    }

    
    /**
     *     Processo que vai na fila
     *
     */
    /**********************************************************/
    static class MyNode extends Thread {

        boolean hasToken;

        int index;
   

        //construtor
        /**********************************************************/
        MyNode(int i, boolean t) {
            this.hasToken = t;
            this.index = i;
            System.out.println("[Nodo " + this.index + "] iniciado");
        }


        //recebe o token
        /**********************************************************/
        public void receiveToken (boolean t) {
        	System.out.println("-------------------------------------------");
        	System.out.println("[Nodo " + this.index + "] recebendo o token");
            this.hasToken = t;
            
            //tenta accessar a regi�o cr�tica
            try {
                accessCritialArea();
            } catch (Exception e) {
                System.err.println("[Nodo " + this.index + "]" + e.getMessage());
                System.exit(0);
            //libera o token 
            } finally {
                this.passToken();
            }
            
        }


        //passa o token para o pr�ximo nodo
        /**********************************************************/
        private void passToken () {
        	System.out.println("[Nodo " + this.index + "] passando o token");
            int nextPos = next[index];
            MyNode next = queue.get(nextPos);
            next.receiveToken(this.hasToken);
            this.hasToken = false;
        }
  

        //acesso a regi�o cr�tica
        /**********************************************************/
        private void accessCritialArea() throws Exception {
        	System.out.println("[Nodo " + this.index + "] Acessando regi�o cr�tica");
        	criticalArea(this);
        }

        
        //inicia o processo
        /**********************************************************/
        public void run() {
        }
    }

}
