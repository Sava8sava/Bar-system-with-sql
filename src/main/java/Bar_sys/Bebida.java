package Bar_sys;

public class Bebida extends Item{
    protected final double taxa = 0.1;

    public Bebida(int num,String desc,double valor ){
        super(num, desc, valor);
    }

    @Override
    public double getItemPrec(int quant){
        //checar para talves usar a taxa no valor
        return valor * quant;
    }

}
