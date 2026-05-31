package com.estruturas;

import com.auxiliar.Fila;
import com.auxiliar.ListaDuplamenteLigada;
import com.auxiliar.No;
import com.auxiliar.Pilha;
import com.modelo.Local;

public class Grafo {
    private ListaDuplamenteLigada vertices; // lista de Vertice

    public Grafo() {
        this.vertices = new ListaDuplamenteLigada();
    }

    // Encontra o Vertice que embrulha um dado Local, ou null.
    private Vertice procuraVertice(Local local) {
        No atual = this.vertices.getPrimeiro();
        while (atual != null) {
            Vertice v = (Vertice) atual.getElemento();
            if (v.getLocal().equals(local)) {
                return v;
            }
            atual = atual.getProximo();
        }
        return null;
    }

    public void adicionaVertice(Local local) {
        if (procuraVertice(local) != null) {
            return; // ja existe este vertice
        }
        this.vertices.adicionaFim(new Vertice(local));
    }

    public void adicionaAresta(Local origem, Local destino, double peso) {
        Vertice vOrigem = procuraVertice(origem);
        Vertice vDestino = procuraVertice(destino);

        if (vOrigem == null || vDestino == null) {
            throw new IllegalArgumentException(
                    "Ambos os locais tem de existir no grafo antes de os ligar.");
        }

        ligar(vOrigem, vDestino, peso);   // origem -> destino
        ligar(vDestino, vOrigem, peso);   // destino -> origem (nao-dirigido)
    }

    // Adiciona uma aresta dirigida origem->destino, evitando duplicados.
    private void ligar(Vertice origem, Vertice destino, double peso) {
        if (existeAresta(origem, destino)) {
            return;
        }
        origem.getAdjacentes().adicionaFim(new Aresta(destino, peso));
    }

    // Verifica se 'origem' ja tem uma aresta para o vertice 'destino'.
    private boolean existeAresta(Vertice origem, Vertice destino) {
        No atual = origem.getAdjacentes().getPrimeiro();
        while (atual != null) {
            Aresta a = (Aresta) atual.getElemento();
            if (a.getDestino() == destino) {   // mesma instancia de vertice
                return true;
            }
            atual = atual.getProximo();
        }
        return false;
    }

    // Reinicia as marcas de todos os vertices antes de cada algoritmo.
    private void limparMarcas() {
        No atual = this.vertices.getPrimeiro();
        while (atual != null) {
            Vertice v = (Vertice) atual.getElemento();
            v.setVisitado(false);
            v.setDistancia(Double.POSITIVE_INFINITY);
            v.setAnterior(null);
            atual = atual.getProximo();
        }
    }

    //=======BFS======
    public ListaDuplamenteLigada caminhoMenosTrocos(Local origemLocal, Local destinoLocal) {
        Vertice origem = procuraVertice(origemLocal);
        Vertice destino = procuraVertice(destinoLocal);
        if (origem == null || destino == null) {
            return new ListaDuplamenteLigada(); // origem ou destino nao existem
        }

        limparMarcas();

        Fila fila = new Fila();
        origem.setVisitado(true);
        fila.enqueue(origem);

        while (!fila.isEmpty()) {
            Vertice atual = (Vertice) fila.peekAndDequeue();

            if (atual == destino) {
                break; // ja chegamos
            }

            No no = atual.getAdjacentes().getPrimeiro();
            while (no != null) {
                Aresta a = (Aresta) no.getElemento();
                Vertice vizinho = a.getDestino();
                if (!vizinho.isVisitado()) {
                    vizinho.setVisitado(true);
                    vizinho.setAnterior(atual);   // rasto para reconstruir
                    fila.enqueue(vizinho);
                }
                no = no.getProximo();
            }
        }

        if (!destino.isVisitado()) {
            return new ListaDuplamenteLigada(); // nao ha rota
        }
        return reconstroiCaminho(destino);
    }

    // Reconstroi o caminho origem->destino seguindo o rasto 'anterior'.
    private ListaDuplamenteLigada reconstroiCaminho(Vertice destino) {
        ListaDuplamenteLigada caminho = new ListaDuplamenteLigada();
        Vertice atual = destino;
        while (atual != null) {
            caminho.adicionaInicio(atual.getLocal());
            atual = atual.getAnterior();
        }
        return caminho;
    }

    // DFS iterativo (Pilha): conta vertices alcancaveis a partir de 'inicio'.
    private int contaAlcancaveis(Vertice inicio) {
        limparMarcas();

        int contados = 0;
        Pilha pilha = new Pilha();
        pilha.push(inicio);

        while (!pilha.isEmpty()) {
            Vertice atual = (Vertice) pilha.peekAndPop();

            if (atual.isVisitado()) {
                continue; // ja processado; ignora repetidos
            }
            atual.setVisitado(true);
            contados++;

            No no = atual.getAdjacentes().getPrimeiro();
            while (no != null) {
                Aresta a = (Aresta) no.getElemento();
                Vertice vizinho = a.getDestino();
                if (!vizinho.isVisitado()) {
                    pilha.push(vizinho);
                }
                no = no.getProximo();
            }
        }
        return contados;
    }

    // Grafo conexo: todos os vertices alcancaveis a partir de um qualquer.
    public boolean estaTotalmenteLigado() {
        if (this.vertices.tamanho() == 0) {
            return true; // grafo vazio: trivialmente ligado
        }
        Vertice inicio = (Vertice) this.vertices.pega(0);
        int alcancaveis = contaAlcancaveis(inicio);
        return alcancaveis == this.vertices.tamanho();
    }
}