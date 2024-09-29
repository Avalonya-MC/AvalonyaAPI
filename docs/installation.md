# Installation de l'AvalonyaAPI

Suivez ces étapes pour installer et configurer l'AvalonyaAPI sur votre serveur Minecraft.

## Étape 1: Télécharger l'API

1. Compiler la dernière version de cette API en la clonant.
2. Placez le fichier `.jar` dans le dossier `plugins/` de votre serveur Minecraft.

## Étape 2: Configurer la base de données

1. Créez une base de données MySQL.
2. Modifiez le fichier `config.yml` dans le dossier `plugins/AvalonyaAPI/` pour y insérer vos informations de connexion à la base de données :

```yaml
database:
  host: "localhost"
  port: 3306
  user: "root"
  password: "password"
  name: "avalonya_api"
```
## Étape 3: Démarrer le serveur

Lancez le serveur Minecraft et vérifiez que l'API se charge correctement.

---
Vous pouvez maintenant commencer à utiliser l'API. Consultez Vue d'ensemble de l'utilisation pour apprendre à interagir avec les différentes fonctionnalités.