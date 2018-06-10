package main;

public class CriticalRegion implements Runnable {
  static int i = 0;
  private int seconds;
  private Process owner;
  private long id;

  public CriticalRegion(Process owner, int seconds) {
    this.owner = owner;
    this.seconds = seconds; 
    this.id = Thread.currentThread().getId();
  }

  @Override
  public void run () {
    try {
      System.out.println(System.currentTimeMillis() + " Entrando regiao critica - PID - " + this.id  + " - I - " + CriticalRegion.i);
      Thread.sleep(seconds * 1000);
      CriticalRegion.i++;
      System.out.println(System.currentTimeMillis() + " Saindo PID - " + this.id + " - I - " + CriticalRegion.i);
      this.owner.sairRegiaoCritica();
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }
}