package org.example.projetjava;

import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

public class Ecosysteme {
    private List<Animal> animaux;
    private List<ZoneRessource> zonesRessources;
    private List<Vegetal> vegetaux;
    private Saison saisonActuelle;
    public static int tailleCarte; // Taille de la carte (statique pour être utilisée globalement)

    public Ecosysteme(int tailleCarte) {
        this.animaux = new ArrayList<>();
        this.zonesRessources = new ArrayList<>();
        this.vegetaux = new ArrayList<>();
        this.tailleCarte = tailleCarte;
        this.saisonActuelle = new Saison("Été", 1.2, 1.1); // org.example.projetjava.Saison initiale
    }

    public void ajouterAnimal(Animal animal) {
        animaux.add(animal);
    }

    public void ajouterZoneRessource(ZoneRessource zone) {
        zonesRessources.add(zone);
    }

    // Ajouter un végétal
    public void ajouterVegetal(Vegetal vegetal) { vegetaux.add(vegetal); }

    public void changerSaison(Saison nouvelleSaison) {
        saisonActuelle = nouvelleSaison;
        System.out.println("Changement de saison : " + saisonActuelle);
    }

    public void simulerJournee() {
        System.out.println("\n--- Début de la journée ---");
        System.out.println("Saison actuelle : " + saisonActuelle);

        // Mise à jour des animaux
        for (Animal animal : new ArrayList<>(animaux)) { // Copie pour éviter les erreurs de modification
            if (animal.estVivant()) {
                // Interaction avec l'environnement
                animal.interagirAvecEnvironnement();

                Animal bebe = animal.reproduire(animaux);
                if (bebe != null) {
                    animaux.add(bebe);
                    bebe.setPosition(animal.x, animal.y);
                }

                if (animal instanceof Herbivore) {
                    Herbivore herbivore = (Herbivore) animal;

                    // Réagir si un prédateur est détecté
                    herbivore.fuir(animaux);

                    // Former un groupe avec les autres herbivores
                    herbivore.formerGroupe(animaux);

                    // Se nourrir dans les zones de ressources
                    boolean nourri = herbivore.seNourrirDansZone(zonesRessources);
                    if (!nourri) {
                        System.out.println(herbivore.nom + " n'a pas trouvé assez de nourriture ou d'eau.");
                    }
                }

                if (animal instanceof Predateur && !(animal instanceof Humain)) {
                    Predateur predateur = (Predateur) animal;


                    // Traquer et manger des proies
                    boolean aMange = predateur.mangerProie(animaux);
                    if (!aMange) {
                        System.out.println(predateur.nom + " n'a pas trouvé de proies.");
                    }
                }

                if (animal instanceof Humain) {
                    Humain humain = (Humain) animal;
                    humain.cooperer(animaux); // Coopération avec d'autres humains
                    humain.formerGroupe(animaux);
                    humain.agirFaceAuConflit(animaux);
                    // Traquer et manger des proies
                    boolean aMange = humain.chasserHerbivore(animaux);
                    if (!aMange) {
                        System.out.println(humain.nom + " n'a pas trouvé de proies.");
                    }// Gérer les conflits
                }

                // Déplacement et vieillissement
                animal.seDeplacer();
                animal.vieillir();

                // Affichage de l'état de l'animal
                System.out.println(animal);
            } else {
                System.out.println(animal.nom + " est mort.");
                animaux.remove(animal); // Retirer les animaux morts
            }
        }

        // Mise à jour des végétaux
        for (Vegetal vegetal : new ArrayList<>(vegetaux)) {
            vegetal.croitre();
            if (vegetal.estMature()) {
                List<Vegetal> nouvellesPlantes = vegetal.seReproduire((int) (Math.random() * 10)); // Portée de reproduction
                vegetaux.addAll(nouvellesPlantes);
            }
            if (vegetal.energie <= 0) {
                System.out.println(vegetal.type + " à (" + vegetal.x + ", " + vegetal.y + ") est mort.");
                vegetaux.remove(vegetal);
            }
        }

        System.out.println("--- Fin de la journée ---\n");
    }


    private void regenererRessources() { //pas utilisée pour le moment, sert à rajouter de la nourriture ou de l'eau aux ressources naturelles
        for (ZoneRessource zone : zonesRessources) {
            if (zone.getType().equals("Nourriture")) {
                double regeneNourriture = saisonActuelle.ajusterNourriture(20);
                zone.consommer(-regeneNourriture); // Ajout de ressource (quantité négative pour "ajouter")
            } else if (zone.getType().equals("Eau")) {
                double regeneEau = saisonActuelle.ajusterEau(15);
                zone.consommer(-regeneEau); // Ajout de ressource
            }
        }
    }

    public List<Animal> getAnimaux() {
        return animaux;
    }

    public List<Vegetal> getVegetaux() {
        return vegetaux;
    }

    public List<ZoneRessource> getZonesRessources() {
        return zonesRessources;
    }

    public Set<Color> getUniqueSpeciesColors() {
        return animaux.stream()
                .map(Animal::getColor)
                .collect(Collectors.toSet());
    }

    public static void main(String[] args) {
        // Création de l'écosystème
        Ecosysteme ecosysteme = new Ecosysteme(100);



        // Ajout de zones de ressources
        ecosysteme.ajouterZoneRessource(new ZoneRessource("Nourriture", 80, 70, 250));
        ecosysteme.ajouterZoneRessource(new ZoneRessource("Eau", 20, 30, 250));

        // Ajout d'animaux
        Random rand = new Random();
        Humain humain1 = new Humain("Eve", "femelle", "Homo Sapiens", 80.0,
                1, 80, 15.0, 60.0, 15, 30, 90,
                0.7, 0.2);
        humain1.setPosition(rand.nextInt(Ecosysteme.tailleCarte), rand.nextInt(Ecosysteme.tailleCarte));
        ecosysteme.ajouterAnimal(humain1);

        Humain humain2 = new Humain("Adam", "mâle", "Homo Sapiens", 80.0, 1,
                80, 15.0, 60.0, 15, 30, 70,
                0.5, 0.4);
        humain2.setPosition(rand.nextInt(Ecosysteme.tailleCarte), rand.nextInt(Ecosysteme.tailleCarte));
        ecosysteme.ajouterAnimal(humain2);

        // Ajout de végétaux
        Vegetal herbe = new Vegetal("Herbe", 50, 20, 30);
        ecosysteme.ajouterVegetal(herbe);

        Vegetal buisson = new Vegetal("Buisson", 100, 50, 60);
        ecosysteme.ajouterVegetal(buisson);

        Herbivore lapin1 = new Herbivore("Lapin 1", "mâle", "Lapin", 50.0, 0, 10, 15.0, 50, 15, 20);
        lapin1.setPosition(rand.nextInt(Ecosysteme.tailleCarte), rand.nextInt(Ecosysteme.tailleCarte));
        ecosysteme.ajouterAnimal(lapin1);

        Herbivore lapin2 = new Herbivore("Lapin 2", "femelle", "Lapin",50.0, 0, 10, 15.0, 50, 15, 20);
        lapin2.setPosition(rand.nextInt(Ecosysteme.tailleCarte), rand.nextInt(Ecosysteme.tailleCarte));
        ecosysteme.ajouterAnimal(lapin2);

        Herbivore lapin3 = new Herbivore("Lapin 3", "mâle", "Lapin",50.0, 0, 10, 15.0, 50, 15, 20);
        lapin3.setPosition(rand.nextInt(Ecosysteme.tailleCarte), rand.nextInt(Ecosysteme.tailleCarte));
        ecosysteme.ajouterAnimal(lapin3);

        Herbivore lapin4 = new Herbivore("Lapin 4", "femelle", "Lapin",50.0, 0, 10, 15.0, 50, 15, 20);
        lapin4.setPosition(rand.nextInt(Ecosysteme.tailleCarte), rand.nextInt(Ecosysteme.tailleCarte));
        ecosysteme.ajouterAnimal(lapin4);

        Predateur chacal = new Predateur("Chacal", "mâle", "Chacal", 60.0, 1,
                15, 25.0, 70, 5, 25);
        chacal.setPosition(rand.nextInt(Ecosysteme.tailleCarte), rand.nextInt(Ecosysteme.tailleCarte));
        ecosysteme.ajouterAnimal(chacal);

        Predateur lion = new Predateur("Lion", "mâle", "Lion", 100.0, 2,
                10, 20.0, 80, 15,25);
        lion.setPosition(rand.nextInt(Ecosysteme.tailleCarte), rand.nextInt(Ecosysteme.tailleCarte));
        ecosysteme.ajouterAnimal(lion);

        Predateur lionne = new Predateur("Lionne", "femelle", "Lion", 100.0, 2,
                10, 20.0, 80, 15,25);
        lionne.setPosition(rand.nextInt(Ecosysteme.tailleCarte), rand.nextInt(Ecosysteme.tailleCarte));
        ecosysteme.ajouterAnimal(lionne);

        Predateur tigre = new Predateur("Tigre", "mâle", "Tigre", 80.0, 3,
                10, 30.0, 80, 10, 30);
        tigre.setPosition(rand.nextInt(Ecosysteme.tailleCarte), rand.nextInt(Ecosysteme.tailleCarte));
        ecosysteme.ajouterAnimal(tigre);

        // Simulation de plusieurs journées
        for (int jour = 1; jour <= 10; jour++) {
            System.out.println("\n--- JOUR " + jour + " ---");

            ecosysteme.simulerJournee();

            // Changer la saison après 3 jours
            if (jour == 3) {
                ecosysteme.changerSaison(new Saison("Hiver", 0.8, 0.7));
            }

        }
    }


}
