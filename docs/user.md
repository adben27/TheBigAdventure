# Manuel utilisateur du jeu TheBigAdventure

Bienvenue dans le jeu TheBigAdventure.

Vous pourrez vous déplacer avec les flèches directionelles et prendre un objet dans votre inventaire ou taper avec un SWORD, STICK, ou SHOVEL avec la touche Espace.
Vous pourrez visualiser votre inventaire avec la touche `i`.

### Création du fichier .map

Le fichier .map contient au moins une section grid qui doit contenir les instructions, `size` pour la taille de la carte, `data` pour déclarer la carte avec des petits symboles et `encodings` pour définir le bloc voulu pour un petit symbole de la carte (avant data).

Exemple :

```
[grid]
  size: (6 x 5)
  encodings : WALL(W) BRICK(B) FENCE(F) REED(r)
  data: """
  WWWWWW
  F r  W
  W FF W
  B    B
  BBBBBB
  """
```

Et elle doit aussi contenir le joueur, un élément qui doit obligatoirement contenir `player : true`. Par exemple :

```
[element]
	name: John
	player: true                	
    skin: BABA
	position: (1,1)
	health: 25
```

On peut aussi ajouter un SWORD ou un STICK ou un SHOVEL. Par exemple :

```
[element]
	name: baton
	skin: STICK
	position: (3,1)
	kind: item
	damage: 15
```

L'attribut name n'est pas obligatoire.

On peut aussi rajouter un ennemi pour le taper ! Par exemple :

```
[element]
	name: Waldo
	skin: BADBAD
	position: (1, 3)
	kind: enemy
	health: 10
	damage: 6
```

On peut aussi rajouter des objets d'inventaire comme des objets ou de la nourriture, mais je ne suis pas sûr qu'il seront bien implémentés :/


