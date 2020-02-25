package com.diego.lina.sistemadealmacenes.Entidades;

public class inventario_fisico {
    private String num_parte;
    private String Desc_Merca;
    private String entrada;
    private String salida;
    private String saldo;
    private String ume;

    public String getNum_parte() {
        return num_parte;
    }

    public void setNum_parte(String num_parte) {
        this.num_parte = num_parte;
    }

    public String getDesc_Merca() {
        return Desc_Merca;
    }

    public void setDesc_Merca(String desc_Merca) {
        Desc_Merca = desc_Merca;
    }

    public String getEntrada() {
        return entrada;
    }

    public void setEntrada(String entrada) {
        this.entrada = entrada;
    }

    public String getSalida() {
        return salida;
    }

    public void setSalida(String salida) {
        this.salida = salida;
    }

    public String getSaldo() {
        return saldo;
    }

    public void setSaldo(String saldo) {
        this.saldo = saldo;
    }

    public String getUme() {
        return ume;
    }

    public void setUme(String ume) {
        this.ume = ume;
    }
}
