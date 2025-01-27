package Bar_sys;

public class Conta {
    double bebida = 0;
    double comida = 0;
    double tipo_1 = 0;
    double aux_pagamento = 0;
    double aux_final = 0;

    public void addInConta(double valor,int tp){
        if(tp == 1){
          this.tipo_1 += valor;
        }
        else if(tp == 2){
            this.bebida += valor;
        }
        else if (tp == 3){
            this.comida +=  valor;
        }
    }

    public double getBebida() {
        return bebida;
    }

    public double getComida() {
        return comida;
    }

    public double getContafinal() {

        //aux_final = (bebida + comida ) - aux_pagamento;
        aux_final = tipo_1 + (bebida + (0.1 * bebida) + comida + (0.15 * comida)) - aux_pagamento;

        return aux_final;
    }

    public void setPagamento(double valor){
        this.aux_pagamento += valor;
    }

    public void setAux_final(double aux_final) {
        this.aux_final = aux_final;
    }
    public void setAux_pagamento(double pag){
        this.aux_pagamento = pag;
    }

    public double getAux_pagamento() {
        return aux_pagamento;
    }
}

