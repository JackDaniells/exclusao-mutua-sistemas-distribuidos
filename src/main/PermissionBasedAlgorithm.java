package main;

import java.util.LinkedList;
import java.util.Queue;

import main.RingBasedAlgorithm.MyNode;


public class PermissionBasedAlgorithm {
	
	static boolean token;
   
    //fila de itens
    static LinkedList<MyNode> nodes = new LinkedList<MyNode>();
    
    static Queue<MyNode> nodesWaitingAccess = new LinkedList<MyNode>();
    
    //construtor
    /**********************************************************/
    PermissionBasedAlgorithm(int size) { 
    	token = true;
    	//põe os nodos na fila
        for (int i = 0; i < size; i++) {
        	//cria o nodo
            MyNode node = new MyNode(i);
            //inicia o nodo
            node.start();
            //adiciona o nodo na fila
            nodes.add(node);   
        }
  	
    }  
    
    //requisição de acesso a região critica
    static void requestAccessCritialArea(MyNode n) {
    	//concede o acesso se a fila estiver vazia
    	if(nodesWaitingAccess.isEmpty() && token) {
    		token = false;
    		n.conceedAccessCritialArea(token);
    	} else {
    		//adiciona o nodo a fila caso não esteja com o token
    		nodesWaitingAccess.add(n);
    	}
    }
    
    //devolução do token pelo processo
    /**********************************************************/
    static void backToken(boolean t) {
    	//recebe o token
    	token = t;
    	//verifica se tem nodos na fila
    	if(!nodesWaitingAccess.isEmpty()) {
    		///pega o proximo nodo da fila
    		MyNode next = nodesWaitingAccess.poll();
    		//concede o token para este nodo
    		next.conceedAccessCritialArea(token);
    	}
    }
    
    //representação da região crítica
    /**********************************************************/
    static void criticalArea() throws Exception {
    	Thread.sleep(5000);
    }
    
    

    
    /**
     *     Processo que vai na fila
     *
     */
    
    /**********************************************************/
    static class MyNode extends Thread {

    	boolean token;
        int index;
        boolean isAccessRequested;
   
        //construtor
        /**********************************************************/
        MyNode(int i) {
            this.index = i;
            this.token = false;
            this.isAccessRequested = false;
            System.out.println("[Nodo " + this.index + "] iniciado");
        }

        //tentativa de acesso a região crítica
        /**********************************************************/
        private void requestAccessCritialArea() throws Exception {
        	this.isAccessRequested = true;
        	System.out.println("[Nodo " + this.index + "] Requisitando acesso a RC");
        	PermissionBasedAlgorithm.requestAccessCritialArea(this);
        }
        
        //permissão de acesso a região crítica
        /**********************************************************/
        public void conceedAccessCritialArea(boolean token) {
        	this.token = token; 
        	System.out.println("[Nodo " + this.index + "] Acesso concedido a RC");
        	//acessa a RC
        	try {
        		PermissionBasedAlgorithm.criticalArea();
        	} catch (Exception e) {
        		System.err.println("[Nodo " + this.index + "] Erro na execução");
        	}
    		//devolve o token
    		System.out.println("[Nodo " + this.index + "] devolvendo token");
    		PermissionBasedAlgorithm.backToken(this.token);
        }
        
        //inicia o processo
        /**********************************************************/
        public void run() {
        	try {
        		this.sleep(1000);
        		
        		//requisita o acesso a RC caso ainda nao tenha requisitado
        		if(!isAccessRequested) this.requestAccessCritialArea();
        		
        	} catch (Exception e) {
        		System.err.println( "[Nodo " + this.index + "]Erro na execução");
        	}
        }
    }

}
