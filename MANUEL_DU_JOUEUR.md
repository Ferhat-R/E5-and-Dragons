# Manuel du Joueur - E5 and Dragons (END)

## Introduction

Bienvenue dans **E5 and Dragons (END)**, un jeu de rôle textuel inspiré de Dungeons & Dragons 5e ! Explorez des donjons, combattez des monstres, parlez avec des NPCs, et collectez de l'or dans cette aventure en ligne de commande.

---

## Installation et Lancement

### Prérequis
- **sbt** (Scala Build Tool) installé
- **Java 11+**

### Lancer le jeu
```bash
cd /chemin/vers/E5-and-Dragons
sbt endGame/run
```

Le jeu se lance automatiquement et charge la carte `e5-dungeon.dndmap`.

---

## Contrôles

| Touche | Action |
|--------|--------|
| **W** | Se déplacer vers le Nord |
| **A** | Se déplacer vers l'Ouest  |
| **S** | Se déplacer vers le Sud |
| **D** | Se déplacer vers l'Est |
| **Q** | Quitter le jeu |

---

## Comprendre la Carte

Lorsque vous jouez, une carte s'affiche dans la console :

```
====================
Exploration Map:
P N . . . .
. . 1 . . .
. . . . . .
G . . . . .

Player at (0, 0) facing S
```

### Légende de la carte

| Symbole | Signification |
|---------|---------------|
| **P** | Votre personnage (Player) |
| **N** | NPC (Personnage non-joueur) |
| **1, 2, 3...** | Ennemis combattables (numérotés) |
| **G** | Or à ramasser (Gold) |
| **.** | Case vide |

---

## Mécaniques de Jeu

### 1. Exploration
- Utilisez **W/A/S/D** pour vous déplacer sur la carte
- Votre personnage ne peut pas sortir des limites de la carte
- Chaque déplacement met à jour votre position et votre orientation

### 2. Combat Automatique

Le combat se déclenche **automatiquement** quand vous marchez sur une case avec un ennemi (numéros 1, 2, 3...).

#### Déroulement du combat

1. **Initiative** : Vous et l'ennemi lancez 1d20 pour déterminer qui commence
2. **Tours alternés** : Le combattant avec la plus haute initiative attaque en premier
3. **Jet d'attaque** : Lancer 1d20
   - **Si jet ≥ Classe d'Armure (AC) de l'adversaire** → TOUCHE !
   - **Si jet < AC** → RATE !
4. **Calcul des dégâts** (en cas de touche) :
   - Dégâts de l'action principale (ex: 2d6 pour un Paladin)
   - **+** Dégâts de l'action bonus si disponible (ex: +1d6 pour Paladin niveau > 3)
5. **Fin du combat** :
   - **Victoire** → Vous récupérez l'or de l'ennemi, l'ennemi disparaît de la carte
   - **Défaite** → Message "YOU DIED! GAME OVER"

#### Exemple de combat
```
COMBAT
==========================================
Player: 25/25 HP
Villain: 25/25 HP

Turn: Player
>> Player rolled 16 and HIT for 11 damage!
==========================================

COMBAT
==========================================
Player: 25/25 HP
Villain: 14/25 HP

Turn: Villain
>> Villain rolled 15 and HIT for 10 damage!
==========================================
```

### 3. Interactions Sociales (NPCs)

Quand vous marchez sur une case avec un **N** (NPC), un dialogue s'affiche automatiquement.

#### Exemple de dialogue
```
=== DIALOGUE ===

Marchand ambulant :
"Salutations, voyageur ! Je parcours ces donjons depuis des années..."

Réponses possibles :
  1. Que vendez-vous ?
  2. Avez-vous vu des monstres par ici ?
  3. Au revoir.

================
```

**Note** : Les dialogues sont actuellement informatifs. Le système affiche simplement la conversation, et vous pouvez continuer votre exploration.

### 4. Collecter de l'Or

- **Or des ennemis** : Automatiquement récupéré après une victoire en combat
- **Or sur la carte (G)** : *Fonctionnalité à venir* - Actuellement, l'or est affiché mais pas encore collecté automatiquement

---

## Statistiques du Personnage

Votre personnage possède plusieurs statistiques importantes :

| Statistique | Description |
|-------------|-------------|
| **HP (Hit Points)** | Points de vie. À 0 HP, c'est Game Over |
| **AC (Armor Class)** | Classe d'Armure. Détermine la difficulté de vous toucher |
| **Niveau** | Niveau du personnage |
| **Race** | Race (HUMAN, ELF, DWARF...) |
| **Classe** | Classe (PALADIN, WIZARD, ROGUE...) |
| **Or** | Quantité d'or possédée |

### Classes de personnages

Chaque classe a des actions différentes :

- **PALADIN** : Attaque avec 2d6, Action bonus +1d6 (niveau > 3)
- **WIZARD** : Attaque avec 1d8
- **ROGUE** : Attaque avec 1d6
- *(autres classes selon la configuration)*

---

## Astuces et Stratégies

1. **Évitez les ennemis si vos HP sont bas** - Il n'y a pas (encore) de système de repos ou de potions
2. **Explorez méthodiquement** - Notez où se trouvent les ennemis sur la carte
3. **Parlez aux NPCs** - Ils peuvent donner des informations utiles sur le donjon
4. **L'initiative est cruciale** - Commencer en premier peut faire la différence en combat

---

## Informations Techniques

### Architecture du Projet

Le projet suit une **architecture hexagonale** avec 3 modules principaux :

- **Exploration** : Gestion de la carte et des déplacements
- **Combat** : Système de combat D&D 5e
- **Social Interaction** : Dialogues avec les NPCs

### Fichiers de Configuration

- **`e5-dungeon.dndmap`** : Fichier de carte situé dans `app/end-game/src/main/resources/`

Format du fichier :
```
-- Map of 10(width) x 4(height)
M - 10 - 4

-- NPC coordinates (1,0)
NPC - 1 - 0

-- PC(villain) coordinates (2,1), lvl, class, race, AC, HP
PC - 2 - 1 - 6 - HUMAN - PALADIN - 13 - 25

-- Character coordinates, lvl, class, race, AC, HP, Orientation
C - 0 - 0 - 6 - HUMAN - PALADIN - 13 - 25 - S

-- Gold Pieces coordinates, amount
GP - 0 - 3 - 50
```

---

## Fonctionnalités Futures

- [ ] Système de collecte d'or automatique sur la carte
- [ ] Système d'inventaire (objets, potions, armes)
- [ ] Dialogues interactifs avec choix de réponses
- [ ] Quêtes données par les NPCs
- [ ] Système de repos pour récupérer des HP
- [ ] Plus de types d'ennemis et de NPCs
- [ ] Plusieurs donjons/cartes
- [ ] Système de niveau et d'expérience

---

## Résolution de Problèmes

### Le jeu ne démarre pas
```bash
# Recompiler le projet
sbt clean compile
sbt endGame/run
```

### La carte ne se charge pas
- Vérifiez que `e5-dungeon.dndmap` existe dans `app/end-game/src/main/resources/`
- Vérifiez le format du fichier (voir section "Fichiers de Configuration")

### Erreurs de compilation
```bash
# Nettoyer et recompiler
sbt clean
sbt compile
```

---

## Crédits

**Projet** : E5 and Dragons  
**Architecture** : Hexagonale (Ports & Adapters)  
**Langage** : Scala 3.7.3  
**Build Tool** : sbt  
**Inspiration** : Dungeons & Dragons 5e

---

## Contact et Contribution

Pour toute question ou contribution, consultez le `README.md` principal du projet.

Bon jeu! Que l'aventure commence !
