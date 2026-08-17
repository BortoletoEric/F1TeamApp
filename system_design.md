# SYSTEM DESIGN & PROJECT CONTEXT: F1 APP

## 1. ESCOPO DO PROJETO
* **Objetivo:** Aplicativo Android para listagem de equipes de Fórmula 1 e detalhes de seus pilotos.
* **Estratégia:** Offline-first com banco de dados local atuando como Single Source of Truth (SSOT).
* **API Base:** `https://f1api.dev/pt`
* **Endpoint Principal:** `https://f1api.dev/pt/docs/teams/current-teams`

## 2. STACK TECNOLÓGICA
* **Linguagem:** Kotlin
* **UI:** Jetpack Compose (Navigation Compose)
* **Arquitetura Padrão:** Clean Architecture + MVVM
* **Persistência Local:** Room (SQLite)
* **Rede:** Retrofit
* **Assincronismo:** Coroutines + Flow (StateFlow)
* **Imagens:** Coil

## 3. REGRAS DE NEGÓCIO E REQUISITOS OBRIGATÓRIOS
1. **SSOT (Offline-First):** A UI deve consumir dados exclusivamente do banco de dados local (Room) através de `Flow`. A API serve apenas para atualizar o banco.
2. **Ordenação:** A lista de times deve ser obrigatoriamente ordenada por "Descrição".
3. **Favoritos:** O aplicativo deve permitir favoritar times. Este estado (`isFavorite`) pertence estritamente ao banco local e não deve ser sobrescrito pelas atualizações vindas da API.
4. **Detalhes do Time:** Ao selecionar uma equipe, a aplicação deve exibir os detalhes da equipe e a listagem de seus pilotos correspondentes.

## 4. ESTRUTURA DE DIRETÓRIOS
io.github.bortoletoeric.f1app
├── core/
│   ├── network/    # Configurações do Retrofit
│   ├── exception/  # Tratamento global de exceções
│   └── utils/      # Constantes
├── data/
│   ├── local/      # Room, DAOs e Entities (ex: TeamEntity, DriverEntity)
│   ├── remote/     # Retrofit Services e DTOs da API
│   └── repository/ # Implementações concretas das interfaces de repositório
├── domain/
│   ├── model/      # Modelos de dados puros
│   └── repository/ # Interfaces abstratas dos repositórios
└── presentation/
├── theme/      # Configuração visual do Compose
├── navigation/ # NavHost e Rotas
├── teams/      # Feature: TeamsScreen.kt, TeamsViewModel.kt
└── drivers/    # Feature: DriversScreen.kt, DriversViewModel.kt

## 5. DATA FLOW (FLUXO DE DADOS)
1. **Init:** `TeamsViewModel` assina o `Flow<List<Team>>` do Room via Repositório.
2. **Sync:** Repositório faz o *fetch* da API via Retrofit em background.
3. **Persist:** Dados da API são mapeados para *Entities* e inseridos no Room (Upsert), mantendo os valores de `isFavorite`.
4. **Render:** Room emite a nova lista, o ViewModel atualiza o `UiState`, e o Jetpack Compose re-renderiza a tela reativamente.

## 6. ESTADO ATUAL DO PROJETO
- [X] Configuração do `build.gradle` (dependências: Compose, Room, Retrofit, Coil).
- [X] Criação das camadas `domain/model` e `domain/repository`.
- [X] Criação da camada `data/local` (Entities e DAOs).
- [X] Criação da camada `data/remote` (DTOs e Retrofit Service).
- [X] Implementação do Repositório (`data/repository`).
- [X] Criação da `TeamsViewModel` e `TeamsScreen`.
- [X] Criação da `DriversViewModel` e `DriversScreen`.