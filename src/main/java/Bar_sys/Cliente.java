package Bar_sys;

public class Cliente{
    int num;
    int cpf;
    String Name;
    boolean contaaberta;
    Conta clientConta = new Conta();



    public Cliente(int n,int cpf,String name){
        this.num = n;
        this.cpf = cpf;
        this.Name = name;
        this.contaaberta = true;

    }

    public void addped(double valor,int tp){
        clientConta.addInConta(valor, tp);
    }

    public int getCpf() {
        return cpf;
    }

    public int getNum() {
        return num;
    }

    public boolean isContaaberta() {
        return contaaberta;
    }

    public double getConta(){
        return clientConta.getContafinal();
    }

    public void setContaaberta(boolean contaaberta) {
        this.contaaberta = contaaberta;
    }

    public void setPagamento(double valor){
        clientConta.setPagamento(valor);
    }

    public double getCom(){
        return clientConta.getComida();
    }

    public double getbebi(){
        return clientConta.getBebida();
    }
}

