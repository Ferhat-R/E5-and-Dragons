# E5-and-Dragons - Branche `fight`

## 🎮 Combat System Implementation

Cette branche contient l'implémentation complète du système de combat automatique basé sur les mécaniques D&D 5e.

---

## 🚀 Lancer le Jeu

### Prérequis
- **sbt** installé (Scala Build Tool)
- Java 24+

### Installation de sbt (si nécessaire)
```bash
# Via Coursier (recommandé)
curl -fL https://github.com/coursier/launchers/raw/master/cs-x86_64-pc-linux.gz | gzip -d > cs && chmod +x cs && ./cs setup --yes

# Ajouter au PATH
source ~/.profile
```

### Lancer le jeu
```bash
sbt endGame/run
```

---

## 🎯 Contrôles

| Touche | Action |
|--------|--------|
| **W** | Déplacement Nord ⬆️ |
| **A** | Déplacement Ouest ⬅️ |
| **S** | Déplacement Sud ⬇️ |
| **D** | Déplacement Est ➡️ |
| **Q** | Quitter le jeu |

---

## ⚔️ Comment Déclencher un Combat

Le combat se déclenche **automatiquement** quand vous marchez sur une case avec un ennemi.

### Position de l'ennemi
```
P N . . . . . . . .    P = Vous (0,0)
. . 1 . . . . . . .    1 = Ennemi (2,1)
. . . . . . . . . .    N = NPC (1,0)
G . . . . . . . . .    G = Or (0,3)
```

### Commandes pour atteindre l'ennemi
Depuis la position de départ (0,0) :
```
D   # Déplacement Est → (1,0)
D   # Déplacement Est → (2,0)
S   # Déplacement Sud → (2,1) → ⚔️ COMBAT !
```

---

## 🎲 Système de Combat

Le combat suit les règles D&D 5e simplifiées :

### 1. Initiative
- Chaque combattant lance 1d20
- Le plus haut score commence en premier

### 2. Tour d'attaque
- **Jet d'attaque** : 1d20
- **Si jet ≥ Classe d'Armure (AC) de l'adversaire** : TOUCHÉ !
  - Calcul des dégâts avec l'action principale (ex: 2d6 pour Paladin)
  - + Dégâts de bonus action si disponible (Paladin niveau > 3 : +1d6)
- **Si jet < AC** : RATÉ !

### 3. Fin du combat
- Combat continue jusqu'à ce qu'un combattant atteigne 0 HP
- **Victoire** : Vous récupérez l'or de l'ennemi, l'ennemi disparaît de la carte
- **Défaite** : Message "YOU DIED! GAME OVER"

---

## 📊 Exemple de Combat

```
⚔️  COMBAT ⚔️
========================================
Player: 25/25 HP
Villain: 25/25 HP

Turn: Player
>> Player rolled 16 and HIT for 11 damage!
========================================

⚔️  COMBAT ⚔️
========================================
Player: 25/25 HP
Villain: 14/25 HP

Turn: Villain
>> Villain rolled 15 and HIT for 10 damage!
========================================
```

---

## 🏗️ Architecture du Code

### Modules Modifiés

#### 1. **FightingEngine** (`core/combat/src/main/scala/domain/FightingEngine.scala`)
- Gère la logique complète du combat
- Initiative, attaques, dégâts, gestion des tours
- Retourne victoire ou mort

#### 2. **MovementEngine** (`core/exploration/src/main/scala/domain/MovementEngine.scala`)
- Détecte les collisions avec ennemis
- Déclenche automatiquement le combat
- Met à jour la carte après victoire/défaite

#### 3. **RandomnessAdapter** (`infra/src/main/scala/randomness/MachineDefaultRandomnessAdapter.scala`)
- Génère les jets de dés (d20, d6)
- Utilise `scala.util.Random`

#### 4. **ConsoleRenderingAdapter** (`infra/src/main/scala/rendering/ConsoleRenderingAdapter.scala`)
- Affiche l'état du combat à chaque tour
- Montre HP, jets de dés, et résultats

#### 5. **Main** (`app/end-game/src/main/scala/Main.scala`)
- Câble toutes les dépendances
- Lance le jeu avec combat intégré

---

## 📦 Dépendances

Modification du `build.sbt` :
```scala
lazy val exploration =
  (project in file("core/exploration"))
    .dependsOn(commons, combat)  // combat ajouté !
```

Ceci permet à `MovementEngine` d'utiliser `FightingEngine`.

---


## 📝 Fichiers Modifiés dans cette Branche

| Fichier | Modifications |
|---------|---------------|
| `app/end-game/src/main/scala/Main.scala` | Ajout FightingEngine et RandomnessAdapter |
| `build.sbt` | Dépendance combat ajoutée à exploration |
| `core/combat/src/main/scala/domain/FightingEngine.scala` | Implémentation complète du combat D&D 5e |
| `core/combat/src/main/scala/model/FightState.scala` | Modèle d'état de combat avec HP et actions |
| `core/exploration/src/main/scala/domain/MovementEngine.scala` | Détection d'ennemis et déclenchement de combat |
| `infra/src/main/scala/randomness/MachineDefaultRandomnessAdapter.scala` | Jets de dés aléatoires |
| `infra/src/main/scala/rendering/ConsoleRenderingAdapter.scala` | Affichage des états de combat |

**Total** : 7 fichiers modifiés, ~170 lignes ajoutées/modifiées

---

## 🎯 Prochaines Étapes Possibles

- [ ] Ajouter plus de types d'ennemis
- [ ] Implémenter un système d'inventaire
- [ ] Ajouter des sorts et capacités spéciales
- [ ] Créer un système de niveau (level up)
- [ ] Ajouter des dialogues avec NPCs
- [ ] Créer plus de cartes/donjons

---

## 🐛 Problèmes Connus

Aucun problème connu pour le moment. Le système compile et fonctionne correctement.

---

## 📖 Plus d'Informations

Pour plus de détails sur l'implémentation, consultez le code source ou les commentaires dans le commit principal de cette branche.

**Commit message** :
```
feat: Implement automatic combat system with D&D 5e mechanics
```

---

**Bon jeu ! ⚔️🎲**
