# SYSTEM DESIGN & PROJECT CONTEXT: F1 APP (V2 - Refatoração)

## 1. ESCOPO DO PROJETO
* **Objetivo:** Aplicativo Android para listagem do campeonato de construtores de Fórmula 1 e detalhes estatísticos de seus pilotos.
* **Estratégia:** Offline-first com banco de dados local atuando como Single Source of Truth (SSOT).
* **API Base:** `https://f1api.dev/pt`
* **Endpoint Principal (Standings):** `https://f1api.dev/pt/docs/standings/current-constructors-championship`
* **Endpoint Detalhes (Drivers):** `https://f1api.dev/pt/docs/teams/{teamId}/drivers`

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
2. **Ordenação (Equipes):** A regra anterior de ordenação por "Descrição" está descartada. A lista de equipes deve respeitar estritamente a ordenação nativa da API (posição no campeonato/pontos).
3. **Ordenação (Pilotos):** A lista de pilotos na tela de detalhes deve ser obrigatoriamente ordenada por pontos de forma decrescente. Operação restrita ao `DriversViewModel`.
4. **Favoritos:** O aplicativo deve permitir favoritar equipes. Este estado (`isFavorite`) pertence estritamente ao banco local e não deve ser sobrescrito pelas atualizações vindas da API na rotina de Upsert.

## 4. ESTRUTURA DE DIRETÓRIOS (Foco Refatoração)
io.github.bortoletoeric.f1app
├── core/
│   ├── network/    
│   ├── exception/  
│   └── utils/      
├── data/
│   ├── local/      # Room, DAOs e Entities (TeamEntity atualizada para suportar pontos/posição)
│   ├── remote/     
│   │   └── dto/    # ConstructorsChampionshipResponseDto, ConstructorStandingDto (NOVOS)
│   └── repository/ # Implementações concretas 
├── domain/
│   ├── model/      # Modelos de dados atualizados (Team agora reflete o Standing)
│   └── repository/ # TeamRepository (contrato atualizado)
└── presentation/
    ├── theme/      
    ├── navigation/ 
    ├── teams/      # TeamsScreen.kt, TeamsViewModel.kt (Sem lógica de ordenação)
    └── drivers/    # DriversScreen.kt, DriversViewModel.kt (Com lógica de ordenação)

## 5. DATA FLOW (FLUXO DE DADOS)
1. **Init:** `TeamsViewModel` assina o `Flow<List<Team>>` do Room via Repositório.
2. **Sync:** Repositório faz o *fetch* da API (`api/current/constructors-championship`) em background.
3. **Persist:** Dados da API são mapeados. O objeto aninhado `team` é extraído junto com os dados estatísticos. Ocorre a inserção no Room (Upsert), mantendo os valores de `isFavorite`.
4. **Render (Equipes):** Room emite a nova lista já ordenada pelo campeonato, o ViewModel atualiza o `UiState`, e o Compose renderiza a tela.
5. **Render (Pilotos):** Ao selecionar a equipe, `DriversViewModel` aciona `api/current/teams/{teamId}/drivers`, aplica `sortedByDescending { it.points }` em memória e expõe o estado atualizado para a UI.

## 6. ROADMAP DE REFATORAÇÃO
- [X] Configuração inicial e arquitetura base.
- [X] **Data Layer (Remoto):** Excluir DTOs obsoletos de times. Criar `ConstructorsChampionshipResponseDto` e `ConstructorStandingDto`.
- [X] **Data Layer (Remoto):** Atualizar o Retrofit Service para apontar para `/current/constructors-championship`.
- [ ] **Domain Layer:** Atualizar a entidade de domínio `Team` para refletir campos como `points`, `position` e `wins`. Atualizar o contrato `TeamRepository.kt`.
- [ ] **Data Layer (Local/Repository):** Ajustar `TeamEntity` e a lógica de Upsert no Room. Garantir o mapeamento correto extraindo dados do nó aninhado.
- [ ] **Presentation (Pilotos):** Implementar `val sortedDrivers = response.drivers.sortedByDescending { it.points }` no `DriversViewModel.kt`.
- [ ] **Presentation (Equipes):** Remover qualquer ordenação alfabética residual do `TeamsViewModel.kt`.
