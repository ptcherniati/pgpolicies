# pgpolicies
Utilisation des policies dans postgresql

Ce projet permet de montrer un exemple des policies de postgresql.
Les policies permettent de limiter dans postgresql l'affichage de certaines lignes en fonction de règles sql.
Ces règles s'appliquant pour une table un role et une action déterminée.

La présentation est [ici](https://github.com/ptcherniati/pgpolicies/blob/master/doc/droits%20postgresql.odp)

Le projet est côté back-end créé en java avec spring-boot avec spring-security et spring-rest.

Le rôle du back end est d'exposer la base de données sous forme de web service en gérant l'accès à la table aliment avec des policies. Sont but et de montrer un pattern d'utilisation des policies : on crée une restriction d'accès dans la table policies, ce qui crée un role auquel on attribut une policy. Dans un deuxième temps on affecte des droits aux utilisateur (case à cocher) ce qui ammène à rajouter le groupe policy correspondant à l'utilisateur.

L'utilisateur accède ainsi à la base avec son rôle postgresql.



Côté front-end, utilisation de vuejs

Base de données : utiliser le [script de création de la table](https://github.com/ptcherniati/pgpolicies/blob/master/src/main/resources/data.sql)
compiler le front end (on peut utiliser visual studio code):
`cp vuejs
npm run build`

mettre à jour les information de la base de données dans [application.properties](https://github.com/ptcherniati/pgpolicies/blob/master/src/main/resources/application.properties)

compiler la backend 
`mvn clean install`

lancer le backend
se replacer dans le dossier parent 
`cd ..
mvn spring-boot:run`

lancer [l'application](http://localhost:9090/static/index.html)
[api swagger](http://localhost:9090/swagger-ui.html)



