package org.example;

public class Circulo {

    public double raio;

    public Circulo(double raio) {
        if (raio <= 0) {
            throw new IllegalArgumentException(
                    "O valor do raio deve ser maior que zero."
            );
        }

        this.raio = raio;
    }

    public double getRaio() {
        return raio;
    }

    public void setRaio(double raio) {
        if (raio > 0) {
            this.raio = raio;
        } else {
            throw new IllegalArgumentException("Raio informado inválido");
        }
    }

    public double calcularArea() {
        return Math.PI * Math.pow(this.raio, 2);
    }
}