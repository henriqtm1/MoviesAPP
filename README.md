# MoviesAPP

## 📋 Índice

- [Sobre](#-Sobre)
- [Tecnologias utilizadas](#-Tecnologias-utilizadas)
- [Preview](#-Preview)

---

## 🖥 Preview 

<div align="center">
  
<!-- Imagem 1 -->
<img src="https://github.com/henriqtm1/MoviesAPP/assets/69311308/d3380221-aeed-49be-8bdd-d1eb56bb3a93" alt="foto1" width="200" />

<!-- Imagem 2 -->
<img src="https://github.com/henriqtm1/MoviesAPP/assets/69311308/969242c0-418b-4a07-bea9-9b990c8619ce" alt="foto2" width="200" />

<!-- Imagem 3 -->
<img src="https://github.com/henriqtm1/MoviesAPP/assets/69311308/c3b6e6be-40e0-4fac-9901-8b3c46c8b031" alt="foto3" width="200" />
</div>

---

## 📖 Sobre 

A proposta do projeto é a criação de um app onde possa visualizar os filmes em cartaz nos cinemas.

--- 

## 🚀 Tecnologias utilizadas

O projeto foi desenvolvido utilizando as seguintes tecnologias:

- Kotlin
- XML (Constraint Layout)
- Injeção de dependência Koin
- Arquitetura MVVM
- Testes unitários
- Corountines
- Retrofit
- Glide
- Lifecycle
- Navigation Fragment
- Recycler View
---

OBS: O projeto foi configurado com a base url mocada no build gradle, pois se fosse o caso de possuir uma url pra dev e uma pra prod fiz a configuração para alternar no projeto quando estiver na build variant desta forma (release -> pega url de prod) e (debug -> pega url de dev), mas como são iguais utilizam a mesma url em ambas.

Configurei também uma sigla de url na apiModule, para quando tiver um projeto com mais de uma url pra consumir basta utilizar a sigla para escolher qual url o service irá utilizar.

--- 
Desenvolvido por Henrique Marinho Teixeira.
