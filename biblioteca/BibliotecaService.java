package com.pao.laboratory05.biblioteca;

import java.util.Arrays;
import java.util.Comparator;

public class BibliotecaService {
    private Carte[] carti = new Carte[0];

    private BibliotecaService(){}

    private static class Holder{
        private static final BibliotecaService instance = new BibliotecaService();
    }
    public static BibliotecaService getInstance(){
        return Holder.instance;
    }

    public void addCarte(Carte carte){
        Carte[] copyCarti = new Carte[carti.length + 1];
        System.arraycopy(carti, 0 , copyCarti, 0 , carti.length);
        copyCarti[carti.length] = carte;
        carti = copyCarti;
        System.out.println("Carte adăugată: " + carte.getTitlu());
    }

    public void listSortedByRating(){
        Carte[] copyCarti = carti.clone();
        Arrays.sort(copyCarti);
        for(int i=0; i< copyCarti.length; i++){
            System.out.println((i + 1) + ". " + copyCarti[i]);
        }
    }

    public void listSortedBy(Comparator<Carte> comparator){
        Carte[] copyCarti = carti.clone();
        Arrays.sort(copyCarti, comparator);
        for(int i = 0; i < copyCarti.length; i++){
            System.out.println((i + 1) + ". " + copyCarti[i]);
        }
    }

}
