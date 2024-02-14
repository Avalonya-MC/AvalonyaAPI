# Citizen

Citizen est un modèle qui représente un citoyen d'une ville, il existe seulement dans le contexte d'une ville (quand un joueur a rejoint une ville).

## Utilisation
```java
Citizen citizen = new Citizen(player);
Town town = new Town("MyTown", citizen); // Crée une ville avec le nom "MyTown" et le citoyen comme maire

citizen.setTown(town); // Définit la ville du citoyen
```