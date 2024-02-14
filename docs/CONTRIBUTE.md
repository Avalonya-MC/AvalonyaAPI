# Comment contribuer ?

## Prerequis 
- Avoir un compte GitHub
- Avoir [Git](https://git-scm.com/downloads) installé sur votre machine

## Etapes
1. Cloner le projet sur votre machine
2. Prendre un ticket sur le Jira
3. Créer une branche pour votre contribution (`git checkout -b feat/ava-[ticket_number]-[branch_title]`)
4. Faire vos modifications
5. Faire un commit (`git commit -m "[commit_message]"`)
6. Pousser votre branche (`git push --set-upstream origin feat/ava-[ticket_number]-[branch_title]`)
7. Créer une Pull Request sur GitHub (mettre le label `daft` si vous n'avez pas terminé votre contribution)
8. Attendre la review de votre Pull Request

## Règles
- Ne pas push directement sur la branche `main` ou `dev`
- Respecter les conventions de nommage des branches
- Ne pas retirer le label `daft` tant que la contribution n'est pas terminée et que le code n'est pas testé