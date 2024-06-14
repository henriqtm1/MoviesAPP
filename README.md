# MoviesAPP

## 📋 Índice

- [Sobre](#-Sobre)
- [Tecnologias utilizadas](#-Tecnologias-utilizadas)
- [Preview](#-Preview)
- [Considerações adicionais](#-Considerações-adicionais)

---

## 🖥 Preview 

<div align="center">
  
<!-- Imagem 1 -->
<img src="https://github.com/henriqtm1/MoviesAPP/assets/69311308/ceac84e1-e9fa-4239-a695-3d3a898c7900" alt="foto1" width="240" />

<!-- Imagem 2 -->
<img src="https://github.com/henriqtm1/MoviesAPP/assets/69311308/e83922fd-4704-422f-b48d-0e7c818852b4" alt="foto2" width="240" />

<!-- Imagem 3 -->
<img src="https://github.com/henriqtm1/MoviesAPP/assets/69311308/ffd97145-1adc-42b2-8690-c4f52ae04a4d" alt="foto3" width="240" />
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
- Clean Code
- Retrofit
- Glide
- Lifecycle
- Navigation Component
- Recycler View
- ViewBinding
---

## Considerações adicionais
O projeto foi configurado com a base url mocada no build gradle, pois se fosse o caso de possuir uma url pra dev e uma pra prod fiz a configuração para alternar no projeto quando estiver na build variant desta forma (release -> pega url de prod) e (debug -> pega url de dev), mas como são iguais utilizam a mesma url em ambas.

Configurei também uma sigla de url na apiModule, para quando tiver um projeto com mais de uma url pra consumir basta utilizar a sigla para escolher qual url o service irá utilizar.

O aplicativo possui internacionalização para inglês, espanhol e português.

--- 
Desenvolvido por Henrique Marinho Teixeira.
