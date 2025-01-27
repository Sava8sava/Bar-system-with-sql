package Bar_sys;

public class Item {
    protected int Numped;
    protected String Desc;
    protected double valor;

    public Item(int num,String desc,double valor){
        this.Numped = num;
        this.Desc = desc;
        this.valor = valor;
    }

    public int getNumped() {
        return Numped;
    }

    public double getItemPrec(int quant){
        return valor * quant;
    }

    public double getValor() {
        return valor;
    }

    public String getDesc() {
        return Desc;
    }
}
