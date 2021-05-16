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

``./gradlew server:run --args '[nbOfPlayers] [nbOfBots] [port=51769]'`` Pour instancier un serveur

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

- **Pêche**: si un joueur possède une canne à pêche et un morceau de lac, il peut y pécher des **poissons**. Le rendement de la pêche est aléatoire, mais le poisson se vend pour un bon prix sur le marché. La pêche est donc un excellent moyen d'arrondir ses fins de mois!

- **Sabotage** des autres joueurs: le monde de l'agriculture est rude, il faut redoubler d'imagination pour vaincre la concurrence. Il est possible d'acheter des tubes de criquets sur le marché et les lacher astucieusement dans les champs des voisins. Un champ infesté de criquets verra sa production diminuer exponentiellement, jusqu'à ne plus rien produire du tout! Les criquets se propagent également aux champs voisins. Il est donc impératif d'acheter et d'appliquer un **répulsif** sur chaque champ infecté le plus rapidement possible afin de limite les dégats.

- Différents types de **Structures** qui donnent des bonus: construire ses champs à côté de sources d'eau leur procure un bonus de pousse. Voilà pourquoi, en plus de la pêche, s'étendre près de lacs est une bonne initiative! Malheureusement, tout le monde n'a pas cette chance. Voilà pourquoi le joueur peut également construire un arroseur automatique qui va simuler la présence d'un lac et donner aux champs voisins un boost!

- Des conditions de **défaite**: attention de ne pas faire faillite! Des coûts de maintenance s'appliquent aux bâtiments tout les mois. Si le joueur gère mal son budget et sa balance devient négative à la fin d'un tour, il perd, et le jeu continue sans lui!



## Contributions

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

- Testing, debugging et balancing de l'inflation du prix des structures et ressources

- Partie *Fonctionnement du jeu* du README



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

Le jeu a été réalisé avec **LibGDX**, une librairie graphique spécialisée en jeux vidéos. Cette dernière nous permet de faciliter l'affichage des sprites du jeu en considérant chaque objet comme des **acteurs** dans une **scène**. On peut modifier leur position sur la scène, leurs propriétés graphique, capturer les inputs de l'utilisateur (touches du clavier, clics de souris, scroll de la molette...). On peut utiliser des acteurs préfaits de libGDX comme des boutons, des checkbox, des barres de défilement... Elle nous permet également de jouer de la musique.

La classe **ManagementGame** est la classe principale du jeu. Elle est instanciée par **DesktopLauncher**, et c'est cette classe qui charge les ressources du jeu grâce à **MyAssetManager**. Elle charge ensuite **MainMenuScreen**, écran sur lequel l'utilisateur peut rentrer une IP et un port pour se connecter à un serveur, ou quitter le jeu.

La classe **Server** est un serveur dédié pour le jeu. Elle est executée séparément du reste du jeu, a son propre *main()*, etc. On peut la démarrer avec un port, un nombre de joueurs et de bots spécifiques. C'est elle qui se charge de garder la **Map** et les **Inventory** des joueurs. Elle attend en permanence des rêquetes des clients, qui veulent par exemple récupérer la **Map** pour l'affichage, effectue l'action appropriée, et renvoie un status.

La classe **MainGameScreen** est instanciée par **MainMenuScreen** lorsque le joueur se connecte a un serveur. C'est elle qui se charge de l'affichage de la scène de jeu, de récupérer les input des utilisateurs, de mettre a jour constemment la carte et l'inventaire du joueur, etc.

La communication réseau avec **Server** se fait grâce a **ServerInteraction**. Cette classe agit un peu comme une API. C'est elle qui se connecte au socket de **Server**. Elle possède plusieurs méthodes synchronisées que le jeu peut appeller a n'importe quel moment a travers son instance, comme *getMap()*, *getInventory()*, *requestBuyTerrain()*, *requestBuildStructure()*, etc. Ces fonctions vont envoyer un ID unique a **Server**, qui va à son tour executer sa méthode du même nom. Appeler **ServerInteraction**.buyItem(params) revient donc a executer **Server**.buyItem(params).

Le jeu est en grande partie sécurisé contre les tentatives de triche. Toutes les valeurs sensibles sont stockées côté serveur, et toutes les modifications à ces valeurs sont contrôlées par le serveur pour déterminer si elles sont autorisées. Par exemple, si le serveur reçoit une requête d'un joueur pour acheter un terrain, mais que ce n'est pas son tour, alors la requête est ignorée.

La carte du jeu, **Map**, est composée de deux tableaux de **Block**, les cases du jeu: un tableau pour les structures comme les champs ou les enclots, et un pour les environnements comme les plaines ou les lacs. Les structures sont dessinées par dessus les environnements. Chaque **Block** est responsable de gérer les inputs de l'utilisateur qu'il reçoit. Le principe d'hérédité a énormément été utilisé.
Voici l'arbre d'hérédité pour les blocks:
- Block
    - Structure
        - Field
            - TreeField
        - HQ
        - Pasture
        - Sprinkler
    - Environment
        - Plain
        - Lake

Et pour les ressources:
- Resources
    - Animal
        - Sheep
        - Cow
    - Plant
        - Carrot
        - Potato
        - Wheat
        - Wood
    - Grain
        - CarrotSeeds
        - PotatoSeeds
        - WheatSeeds
        - TreeSeeds
    - Item
        - Crickets
        - Repulsive
        - FishRod
    - Product
        - Meat
        - Leather
        - Wool

Ceci permet une flexibilité raisonnable: si on veut ajouter un objet, il suffit d'ajouter une classe qui étend Item, et de faire quelques modifications mineures comme l'ajouter dans l'inventaire.
**Map** est sérialisable, comme tout les blocks et toutes les ressources. C'est ça qui permet au serveur de transférer facilement au client la carte, son inventaire, etc.

**com.warmwaffles.noise.prime.PerlinNoise** est une librairie ajouté au projet pour nous permettre de générer du bruit de perlin pour la génération procédurale de la carte, afin d'avoir des lacs et des forêts aléatoires, mais avec moins de chaos qu'une fonction de bruit totalement aléatoire.


## Difficultés et problèmes non résolus

---
- Les threads et leur concurrence n'ont pas été bien gérés. Des problèmes liés au threading ont été résolus en utilisant des raccourcis probablement peu recommandés.

- Malgré la résolution de multiples bugs de concurrence des threads, il exite toujours dans le jeu un bug lié à la sérialisation de l'inventaire qui provoque un *StreamCorruptedException* et provoque un crash du jeu. Ce bug n'est pas fréquent, et semble arriver de manière totalement arbitraire et aléatoire, ce qui le rend très difficile à traquer.

- Nous avons également eu une instance d'un bug lié a la librairie **LibGDX** elle même et qui n'a jamais pu être reproduit.

---
