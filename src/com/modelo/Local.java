package com.modelo;

public class Local {
    private final String nome;
    private final double latitude;
    private final double longitude;

    public Local(String nome, double latitude, double longitude) {
        this.nome = nome;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getNome() {
        return this.nome;
    }

    public double getLatitude() {
        return this.latitude;
    }

    public double getLongitude() {
        return this.longitude;
    }

    // Dois locais sao o mesmo se tiverem o mesmo nome.
    // Necessario porque a lista usa equals em contem(), e o grafo,
    // BFS, DFS e Dijkstra comparam locais por valor e nao por referencia.
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || this.getClass() != obj.getClass()) {
            return false;
        }
        Local outro = (Local) obj;
        return this.nome.equals(outro.nome);
    }

    // Mantido consistente com equals (convencao do Java).
    @Override
    public int hashCode() {
        return this.nome.hashCode();
    }

    @Override
    public String toString() {
        return this.nome;
    }
}