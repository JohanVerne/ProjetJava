package org.example.projetjava;

import java.util.List;

public class Predateur extends Animal {
    protected double coutChasse;
    protected double gainChasse;

    public Predateur(String nom, String sexe, String espece, double energieInitiale, int niveau, int maxAge,
                     double rapidite, double champDeVision, double coutChasse, double gainChasse) {
        super(nom, sexe, espece, energieInitiale, niveau, maxAge, rapidite, champDeVision);
        this.coutChasse = coutChasse;
        this.gainChasse = gainChasse;
    }

    public Predateur(String nom, String sexe, String espece, double energieInitiale, int niveau, int maxAge,
                     double rapidite, double champDeVision, double coutChasse, double gainChasse, javafx.scene.paint.Color color) {
        super(nom, sexe, espece, energieInitiale, niveau, maxAge, rapidite, champDeVision, color);
        this.coutChasse = coutChasse;
        this.gainChasse = gainChasse;
    }

    @Override
    protected Animal creerBebe(Animal autre) {
        if (autre instanceof Predateur) {
            String nomBebe = "Bébé de " + this.nom + " et " + autre.nom;
            double energieBebe = (this.energie + autre.energie) / 2;
            double rapiditeBebe = (this.rapidite + autre.rapidite) / 2;
            double champDeVisionBebe = (this.champDeVision + autre.champDeVision) / 2;
            double coutChasseBebe = (this.coutChasse + ((Predateur) autre).coutChasse) / 2;
            double gainChasseBebe = (this.gainChasse + ((Predateur) autre).gainChasse) / 2;

            System.out.println(this.nom + " se reproduit avec " + autre.nom + ". Un bébé prédateur est né !");
            Predateur bebe = new Predateur(nomBebe, sexealeatoire(), this.espece, energieBebe,
                    this.niveau, this.maxAge, rapiditeBebe, champDeVisionBebe,
                    coutChasseBebe, gainChasseBebe, this.color);
            // Enregistrer les parents du bébé
            bebe.parents.add(this);
            bebe.parents.add(autre);
            return bebe;
        }
        return null;
    }


    @Override
    public void seDeplacer() {
        if (this instanceof Humain) {
            super.seDeplacer();
        } else {
            System.out.println(nom + " cherche des proies.");
            super.seDeplacer();
        }
    }

    public boolean mangerProie(List<Animal> animaux) {
        for (Animal autre : animaux) {
            if (autre.estVivant() && !autre.espece.equals(this.espece)) {
                double distance = calculerDistance(autre.getX(), autre.getY());
                if (champDeVision >= distance) {
                    double probReussite = 1 - distance / champDeVision;
                    if (Math.random() < probReussite) { // Succès aléatoire basé sur la distance
                        if (autre instanceof Herbivore) {
                            autre.energie = 0; // La proie est tuée
                            this.energie -= coutChasse;
                            this.energie += gainChasse;
                            System.out.println(nom + " attrape et mange " + autre.nom + ". Énergie gagnée !");
                            return true;
                        }
                        if (autre instanceof Predateur && !(autre instanceof Humain)
                                && autre.getNiveau() < this.niveau) {
                            autre.energie = 0; // La proie est tuée
                            this.energie -= coutChasse;
                            this.energie += gainChasse * 1.5;
                            System.out.println(nom + " attrape et élimine " + autre.nom
                                    + " (prédateur plus faible).");
                            return true;
                        }
                        else if (autre instanceof Humain) {
                            autre.energie = 0; // La proie est tuée
                            this.energie -= coutChasse;
                            this.energie += gainChasse * 1.25;
                            System.out.println(nom + " attrape et mange " + autre.nom);
                            return true;
                        }
                    } else {
                        this.energie -= coutChasse;
                        System.out.println(nom + " tente de chasser " + autre.nom + " mais échoue.");
                    }
                }
            }
        }
        return false; // Aucune proie attrapée
    }


    @Override
    public void interagirAvecEnvironnement() {
        System.out.println(nom + " surveille les alentours pour trouver des proies.");
    }

    @Override
    public String toString() {
        return String.format("Predateur [Nom: %s, Energie: %.2f, Sexe: %s]",
                nom, energie, sexe);
    }
}

