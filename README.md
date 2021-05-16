# Projet de PI4 - Jeu de Gestion

Sujet proposé par Edwin Hamel-de le Court, edwin@irif.fr

Étudiants:

- TEIXEIRA MEDEIROS Claudio - L2 Informatique - G6

- MAUDET Benjamin / *(DaRealNim)* - L2 Informatique - G6

- SY Alassane - L2 Informatique - G6

- VALEE Youri - L2 Informatique - G6



## Description du projet

---

Le jeu est un simulateur de ferme **multijoueur en ligne** tour par tour fait avec **libGDX**, permettant à un nombre (choisi par celui qui host le serveur) de joueurs et de **bots** de créer des **champs de plusieurs plantes**, **des pâturages avec plusieurs animaux** et de **pêcher** sur une **carte générée aléatoirement** au début de chaque partie.

Le **but du jeu** est d'être le **dernier joueur avec un solde positif**, pour le faire, les joueurs doivent **saboter les champs des autres joueurs** avec des insectes, se protéger, **placer leurs champs stratégiquement par rapport aux sources d'eau** et échanger leurs produits dans un **marché obeissant aux lois de l'offre de et de la demande**.



## Utilisation

---

### **Serveur**

``./gradlew server:run [port] [nbOfPlayers] [nbOfBots]`` Pour instancier un serveur

Le jeu commencera une fois que tous les joueurs se connectent.

### Joueur

``./gradlew desktop:run`` Pour instancier un joueur

Dans le menu, le joueur sera presenté avec deux cases `IP`et `Port`. Ces informations lui seront donnés par celui qui host le serveur pour lui permettre de se connecter.

Il est aussi possible de commencer un jeu solo avec des bots en lançant un serveur sur sa machine avec un seul joueur et en se connectant à `127.0.0.1` (localhost)



## Fonctionnalités

---

- Le **jeu de base** comme décrit dans le sujet.

- **Bots** qui jouent à leur tour.

- **Multijoueur** en ligne.

- **Carte aléatoire** générée avec le **bruit de Perlin** pour le placement des environnements.

- **Marché** qui obéit à l'offre et à la demande: le prix de chaque ressource, produit ou objet monte lorsqu'un joueur l'achète, et descend lorsqu'un joueur le vend. Le prix des produits monte constamment pour simuler la demande du monde extérieur. Il en va de même pour le prix des graines, qui lui est constamment tiré vers le bas si les joueurs ne les achètent pas.

- Un système d'**achat de terrains**: le joueur peut s'étendre en achetant les cases adjacentes à celles qu'il possède déjà, ce qui lui permet de construire plus de structures et de produire plus.

- Différents types de **Plantes**: plantez, récoltez et vendez du blé, des pommes de terre et des carottes.

- Différents types d'**Animaux**: il est possible d'élever des vaches ou des moutons dans des enclos. Les animaux se nourissent du blé présent dans votre inventaire. Attention: plus les enclos sont pleins, plus les animaux consomment de blé! Les animaux produisent de la viande à chaque tour, ainsi que du cuir pour les vaches, ou de la laine pour les moutons.

- **Pêche**

- **Sabotage** des autres joueurs

- Différents types de **Structures** qui donnent des bonus: construire ses champs à côté de sources d'eau leur procure un bonus de pousse. Voilà pourquoi, en plus de la pêche, s'étendre près de lacs est une bonne initiative! Malheureusement, tout le monde n'a pas cette chance. Voilà pourquoi le joueur peut également construire un arroseur automatique qui va simuler la présence d'un lac et donner aux champs voisins un boost!

- Des conditions de **défaite**: attention de ne pas faire faillite! Des coûts de maintenance s'appliquent aux bâtiments tout les mois. Si le joueur gère mal son budget et sa balance devient négative à la fin d'un tour, il perd, et le jeu continue sans lui!



## Contributions

Les contributions

---



#### Claudio TEIXEIRA:

- Premiers pas avec **libGDX**, premiers **sprites**, premier graphe **UML**

- Structure du jeu autour de la librairie **Scene2D** de **libGDX**, premières versions des **Screens**, Menu et de Game

- **Refactorisation** de certaines classes abstraites

- Première version du mouvement avec la **caméra**

- Première version du **HUD**

- Ajout des sprites pour **HUD**

- **Refactorisation** de ServerInteraction et Resources

- Première implementation des **tours**

- Implementation du **marché**

- Définition des lois de **changement de prix**

- Ajout des sprites pour **marché**

- Reconfiguration des **tâches Gradlew** pour **Server**

- Implémentation de l'**interaction client et serveur**

- Définition du **protocole de communication entre client et serveur**

- Debugging (Concurrence des thread)

- Ajout de **Musique**

- Implémentation de toute les versions des **Bots**

- Ce Readme :)



#### Benjamin MAUDET:

- Ajout de la grande majorité des **sprites** du jeu.

- **Beaucoup** de debugging.

- Deuxième graphe **UML**.

- Premières version de **Block**, première **structure** (Field) et premier **environnement** (Plain).

- Structurisation des classes abstraites, configuration des **Actors** et **AssetManager** selon la librairie **Scene2D** de libGDX.

- Première implementation des **récoltes**.

- Implementation des **Popups**.

- Deuxième version du mouvement avec la **caméra**.

- **Refactorisation** des **Ressources**.

- Debugging **réseau**

- Finalisation de l'implementation du **réseau** (Concurrence réseau)

- Implementation de **marché** en multijoueur

- Ajout de nouvelles fonctionnalités à différent **blocks** et **structures**

- Ajout de nouvelles **structures**

- Géneration aléatoire de **lacs**, idée et implémentation du **bruit de Perlin**

- Finition du **HUD**

- Implémentation du choix de **l'IP** et **Port**

- Première implémentation des coûts de **maintenance** et du système de **défaite**

- Amélioration et finition du **réseau**



#### Alassane SY:

- Première implementation de la classe abstraite **Ressources**

- Première version des classes concrètes de type **Ressources**

- Introduction du **prix** et **volume** aux **Ressources**

- Ajout des popups pour planter des **Grains**

- Premiers pas sur le support du jeu **multijoueur**

- Implementation du choix du **nombre de joueurs**

- Ajout de la structure **TreeField**

- Implementation de la structure **Sprinkler** et ses bonus

- Ajout des **Items**

- Implementation des **Crickets** (sabotage des champs)

- Balancing sur **Crickets** et **Sprinkler**

- Ajout des **HQ**

- Implementation du placement aléatoire des **HQ** par joueur

- Implementation de la **pêche**, ajout de **Fishing Rod**

- Debugging sur **Structures** et **Items**



#### Youri VALLEE

- Première implemenation de **Map**

- Implementation des **coordonnées**

- Première version de **ServerInteraction**

- Implementation de fonctions sur **Field**

- Implementation de la mécanique de **salvage** (Gagner un peu d'argent en détruisant ses structures)

- Génération aléatoire des **Forêts**

- Ajout des **Pastures**, des **Animals** et leurs **Products**

- Debugging

## Fonctionnement du jeu

---

magic.
