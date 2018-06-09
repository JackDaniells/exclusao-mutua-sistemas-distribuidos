package main;

import java.util.LinkedList;


public class RingBasedAlgorithm {
	//tamanho da fila
	static int queueSize;    
    //fila de itens
    static LinkedList<MyNode> queue;    
    //fila de proximo
    static LinkedList<Integer> next;    
    //contador de acessos a região crítica
    static int criticalAreaCounter = 0;
    
    //construtor
    /**********************************************************/
    RingBasedAlgorithm(int size) { 
    	
    	queue = new LinkedList<MyNode>();
    	next = new LinkedList<Integer>();
    	
    	this.queueSize = size;
    	
    	//põe os nodos na fila
        for (int i = 0; i < queueSize; i++) {
        	//cria o nodo
            MyNode node = new MyNode(i, false);
            //inicia o nodo
            node.start();
            //adiciona o nodo na fila
            queue.add(node);
            //define o ultimo elemento apontando pro primeiro
            if(i == queueSize - 1) next.add(i, 0);
            else next.add(i, i + 1);   
        }

        
        //inicia o token no primeiro nodo
        MyNode first = queue.element();
        first.receiveToken(true);
     }
    
    
    //representação da região crítica
    /**********************************************************/
    private static void criticalArea(MyNode n) throws Exception {
    	if(n.hasToken) {
    		//criticalAreaCounter++;
    		//System.out.println("=====");
    		//System.out.println("Acessos simultâneos a área crítica: " + criticalAreaCounter);
    		//System.out.println("=====");
    		Thread.sleep(5000);
    		//criticalAreaCounter--;
    	} else {
    		System.err.println("Erro de Acesso");
    		throw new Exception("Acesso indevido a região crítica");
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
            
            //tenta accessar a região crítica
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


        //passa o token para o próximo nodo
        /**********************************************************/
        private void passToken () {
        	System.out.println("[Nodo " + this.index + "] passando o token");
            int nextPos = next.get(index);
            MyNode next = queue.get(nextPos);
            next.receiveToken(this.hasToken);
            this.hasToken = false;
        }
  

        //acesso a região crítica
        /**********************************************************/
        private void accessCritialArea() throws Exception {
        	System.out.println("[Nodo " + this.index + "] Acessando região crítica");
        	criticalArea(this);
        }

        
        //inicia o processo
        /**********************************************************/
        public void run() {
        }
    }

}
