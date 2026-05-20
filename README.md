# MoviesAPP

Aplicativo Android para consultar filmes em cartaz usando a API do TMDB. O projeto foi criado como case técnico e evoluído com foco em arquitetura, segurança de configuração, estados de tela, paginação e testes.

## Índice

- [Preview](#preview)
- [Sobre](#sobre)
- [Funcionalidades](#funcionalidades)
- [Arquitetura](#arquitetura)
- [Decisões Técnicas](#decisões-técnicas)
- [Tecnologias](#tecnologias)
- [Configuração Local](#configuração-local)
- [Como Executar](#como-executar)
- [Testes e Qualidade](#testes-e-qualidade)

## Preview

<div align="center">
  
<!-- Imagem 1 -->
<img src="https://github.com/henriqtm1/MoviesAPP/assets/69311308/ceac84e1-e9fa-4239-a695-3d3a898c7900" alt="foto1" width="240" />

<!-- Imagem 2 -->
<img src="https://github.com/henriqtm1/MoviesAPP/assets/69311308/e83922fd-4704-422f-b48d-0e7c818852b4" alt="foto2" width="240" />

<!-- Imagem 3 -->
<img src="https://github.com/henriqtm1/MoviesAPP/assets/69311308/ffd97145-1adc-42b2-8690-c4f52ae04a4d" alt="foto3" width="240" />
</div>

## Sobre

A proposta do projeto é permitir que o usuário visualize filmes em cartaz, navegue pela lista de resultados e consulte detalhes de cada filme, como pôster, título, nota e descrição.

## Funcionalidades

- Listagem de filmes populares/em cartaz.
- Paginação com Paging 3, loading footer e retry.
- Tela de detalhes com informações do filme selecionado.
- Estados explícitos de tela: loading, sucesso, vazio e erro.
- Retry para erro inicial e erro ao carregar novas páginas.
- Internacionalização para português, inglês e espanhol.
- Token da API configurado fora do código versionado.
- Logs de rede habilitados apenas em builds debug.

## Arquitetura

O app segue uma arquitetura MVVM simples, mantendo responsabilidades separadas:

- `ui`: Activities, Fragments, adapters e estados de tela.
- `repository`: contrato e implementação de acesso a dados.
- `api`: services e modelos de resposta da API.
- `model`: modelos usados pela camada de UI/domínio.
- `di`: módulos de injeção de dependência com Hilt.

O fluxo principal da Home usa Paging 3:

- `MoviesPagingSource` carrega as páginas da API.
- `Pager` no `HomeViewModel` expõe `PagingData<Movie>`.
- `PagingDataAdapter` renderiza a lista.
- `LoadStateAdapter` trata loading e retry de novas páginas.

As respostas da API são convertidas para modelos internos (`Movie` e `MoviesPage`) antes de chegarem na UI. Isso evita acoplamento direto entre tela e DTOs da API.

## Decisões Técnicas

- **Hilt** foi usado para manter a injeção alinhada com o ecossistema Android atual, com módulos explícitos para rede e repository.
- **Paging 3** substitui paginação manual, reduzindo estado duplicado na UI e centralizando retry/loading/erro no fluxo oficial da biblioteca.
- **XML Views** foi mantido por coerência com o case original e para preservar o escopo do projeto, sem misturar uma migração visual para Compose.
- **Sem modularização**: o app tem escopo pequeno. Separar em módulos adicionaria custo de build e complexidade sem ganho proporcional.
- **DTOs separados dos modelos da UI**: a camada de repository traduz a resposta da API antes de expor os dados para a tela.
- **Erros tipados**: falhas de timeout, falta de conexão, `401`, erro de servidor e erro desconhecido são mapeadas separadamente.

## Tecnologias

- Kotlin
- Android XML Views
- ConstraintLayout
- RecyclerView
- Paging 3
- View Binding
- Navigation Component com Safe Args
- MVVM
- Flow, ViewModel e Lifecycle
- Coroutines
- Retrofit
- OkHttp
- Gson
- Hilt
- Glide
- Material Components
- JUnit, Mockito e MockWebServer

## Configuração Local

O projeto usa `compileSdk` e `targetSdk` 36 com Android Gradle Plugin 9.2.1 e Gradle 9.4.1.

Para consumir a API do TMDB, configure o token de acesso fora do código versionado. O projeto lê o valor de `local.properties`, variável de ambiente ou propriedade Gradle:

```properties
TMDB_ACCESS_TOKEN=seu_token_do_tmdb
```

Em runtime, o app adiciona o prefixo `Bearer` automaticamente caso ele não seja informado.

A base URL da API fica configurada via `BuildConfig`, permitindo separar valores por build type. Hoje debug e release usam a mesma URL:

```kotlin
https://api.themoviedb.org
```

## Como Executar

1. Instale o Android SDK 36.
2. Configure `TMDB_ACCESS_TOKEN` em `local.properties`, variável de ambiente ou propriedade Gradle.
3. Compile o app:

```bash
./gradlew assembleDebug
```

## Testes e Qualidade

Comandos principais usados para validar o projeto:

```bash
./gradlew testDebugUnitTest
./gradlew compileDebugAndroidTestKotlin
./gradlew lintDebug
./gradlew assembleDebug
./gradlew assembleRelease
```

Cobertura atual dos testes unitários:

- ViewModel expondo `PagingData` carregável.
- PagingSource carregando primeira página e última página.
- PagingSource convertendo falhas em erro tipado.
- Repository mapeando resposta da API para modelos internos.
- Service validando resposta mockada e request gerada com MockWebServer.

---

Desenvolvido por Henrique Marinho Teixeira.
