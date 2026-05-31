package com.estruturas;

import com.auxiliar.ListaDuplamenteLigada;
import com.auxiliar.No;
import com.modelo.Local;

public class TabelaHash {
    private static final int CAPACIDADE = 53; // primo
    private ListaDuplamenteLigada[] tabela;
    private int tamanho; // total de locais guardados

    public TabelaHash() {
        this.tabela = new ListaDuplamenteLigada[CAPACIDADE];
        for (int i = 0; i < CAPACIDADE; i++) {
            this.tabela[i] = new ListaDuplamenteLigada();
        }
        this.tamanho = 0;
    }

    // Espalha sobre o nome inteiro (metodo de Horner) em vez de so a 1a letra.
    // toLowerCase trata "polana" e "Polana" como a mesma chave.
    private int codigoHash(String chave) {
        String s = chave.toLowerCase();
        int codigo = 0;
        for (int i = 0; i < s.length(); i++) {
            codigo = (31 * codigo + s.charAt(i)) % CAPACIDADE;
        }
        return codigo;
    }

    private Local BuscarNo(ListaDuplamenteLigada lista, String nome) {
        No atual = lista.getPrimeiro();
        while (atual != null) {
            Local local = (Local) atual.getElemento();
            if (local.getNome().equalsIgnoreCase(nome)) {
                return local;
            }
            atual = atual.getProximo();
        }
        return null;
    }

    public void adiciona(Local local) {
        int indice = codigoHash(local.getNome());
        ListaDuplamenteLigada lista = this.tabela[indice];

        if (BuscarNo(lista, local.getNome()) != null) {
            return; // ja existe um local com este nome; nao duplica
        }

        lista.adicionaFim(local);
        this.tamanho++;
    }

    public Local consulta(String nome) {
        int indice = codigoHash(nome);
        return BuscarNo(this.tabela[indice], nome);
    }

    public void remove(String nome) {
        int indice = codigoHash(nome);
        ListaDuplamenteLigada lista = this.tabela[indice];

        No atual = lista.getPrimeiro();
        int posicao = 0;
        while (atual != null) {
            Local local = (Local) atual.getElemento();
            if (local.getNome().equalsIgnoreCase(nome)) {
                lista.removePosicao(posicao);
                this.tamanho--;
                return;
            }
            atual = atual.getProximo();
            posicao++;
        }
        // nome nao encontrado: nao faz nada
    }

    public boolean contem(String nome) {
        return consulta(nome) != null;
    }

    public int tamanho() {
        return this.tamanho;
    }

}