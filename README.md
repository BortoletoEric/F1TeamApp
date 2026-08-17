# F1 Team App - Case Técnico

Este projeto é uma aplicação Android nativa desenvolvida como um desafio técnico para demonstrar competências avançadas em arquitetura de software, padrões de projeto modernos e as melhores práticas de desenvolvimento Android.

A aplicação consome a [F1 API](https://f1api.dev/pt) para listar as equipes da temporada atual da Fórmula 1, permitindo favoritar escuderias e visualizar detalhes técnicos de seus respectivos pilotos.

## 🚀 Funcionalidades

- **Lista de Equipes**: Exibição das escuderias atuais ordenadas alfabeticamente.
- **Favoritos**: Funcionalidade de favoritar/desfavoritar equipes com persistência local e feedback visual imediato via Snackbar.
- **Detalhes da Escuderia**: Visualização de pontos, posição no campeonato, vitórias e ano da temporada.
- **Lista de Pilotos**: Listagem detalhada dos pilotos de cada equipe (nome, número, nacionalidade, idade, pontos e posição), ordenada por pontuação.
- **Modo Offline (Offline-First)**: Persistência de dados completa utilizando SQLite para garantir que o app funcione sem internet após a primeira sincronização.
- **Tratamento de Erros Resiliente**: Sistema de erro que diferencia falhas de conexão, timeout e problemas no servidor, com suporte a "Tentar Novamente".
- **Layout Adaptativo**: Interface otimizada para as orientações Retrato (Portrait) e Paisagem (Landscape), com cabeçalhos dinâmicos para melhor aproveitamento de tela.

## 🏗️ Arquitetura e Decisões de Design

O projeto adota uma versão simplificada da **Clean Architecture** combinada com o padrão **MVVM (Model-View-ViewModel)**, focando em separação de responsabilidades e fluxo de dados unidirecional (**UDF**).

### Camadas do Sistema

1.  **Domain (Domínio)**: Contém as regras de negócio puras. É a camada mais estável e independente de frameworks.
    - `Models`: Classes de dados representativas do negócio (ex: `Team`, `Driver`).
    - `Repository Interfaces`: Define os contratos de como os dados devem ser acessados, sem se preocupar com a origem (API ou DB).
2.  **Data (Dados)**: Implementa os contratos do Domínio.
    - `Repository Implementations`: Gerencia a lógica de sincronização (Network-bound resources).
    - `Local (Room/DAOs)`: Persistência em SQLite.
    - `Remote (Retrofit/DTOs)`: Consumo da API REST.
    - `Mappers`: Fazem a ponte entre os modelos de API/DB e os modelos de Domínio, garantindo que a UI nunca lide com estruturas de rede diretamente.
3.  **UI (Apresentação)**: Camada responsável por exibir os dados e capturar intenções do usuário.
    - `ViewModels`: Gerenciam o `UiState` usando `StateFlow`, reagindo a mudanças no banco de dados e disparando sincronizações.
    - `Compose Screens`: Componentes declarativos que reagem ao estado emitido pela ViewModel.

### Estratégia Offline-First (SSOT)
A aplicação implementa o padrão **Single Source of Truth**. A UI observa apenas o banco de dados local. Quando uma sincronização é solicitada, os dados da API são baixados, mapeados e inseridos no banco. Como a UI está observando o banco via `Flow`, ela é atualizada automaticamente assim que a persistência ocorre. Isso garante uma experiência fluida mesmo em condições de rede instável.

## 📁 Estrutura de Diretórios

```text
io.github.bortoletoeric.f1teamapp/
├── data/
│   ├── local/          # Banco de dados Room, Entidades e DAOs
│   ├── mapper/         # Extensões para conversão entre modelos (DTO -> Entity -> Domain)
│   ├── remote/         # Definições do Retrofit (Interface, DTOs)
│   └── repository/     # Implementação concreta da lógica de dados e cache
├── di/                 # Configuração dos módulos Koin (DI)
├── domain/
│   ├── model/          # Modelos de negócio (Data Classes puras)
│   └── repository/     # Interfaces que definem as operações de dados
├── ui/
│   ├── components/     # Widgets Compose genéricos e reutilizáveis
│   ├── drivers/        # Fluxo de exibição de pilotos (ViewModel + Screen)
│   ├── teams/          # Fluxo de listagem de equipes (ViewModel + Screen)
│   └── theme/          # Definições de cores, tipografia e temas (Material 3)
└── util/               # Classes utilitárias e tratamento centralizado de erros
```

## 🛠 Bibliotecas e Justificativas

| Biblioteca | Motivo da Escolha |
| :--- | :--- |
| **Jetpack Compose** | Padrão moderno da Google para UI. Elimina o boilerplate de XMLs e facilita a criação de layouts adaptativos e estados reativos. |
| **Koin** | Framework de Injeção de Dependência leve e pragmático. Diferente do Hilt/Dagger, não exige anotações complexas e tem uma curva de aprendizado menor para este escopo de projeto, sem sacrificar a escalabilidade. |
| **Room** | Abstração oficial sobre o SQLite. Oferece suporte nativo a `Flow`, facilitando a implementação de uma UI reativa baseada em persistência. |
| **Retrofit** | A biblioteca mais robusta e testada para consumo de APIs REST no ecossistema Android. |
| **Coroutines & Flow** | Essenciais para lidar com concorrência e fluxos de dados assíncronos de forma sequencial e legível, evitando o "callback hell". |
| **Kotlinx Serialization** | Biblioteca de serialização oficial da Jetbrain. É mais rápida por não usar reflexão e totalmente integrada ao ecossistema Kotlin. |
| **Coil** | Biblioteca de carregamento de imagens moderna e leve, otimizada para o Jetpack Compose e baseada em Coroutines. |

## ⚙️ Como Executar

1. Clone o repositório.
2. Certifique-se de estar usando o **Android Studio Ladybug (ou superior)**.
3. Sincronize o projeto com o Gradle.
4. Execute o app em um emulador ou dispositivo real com **Android 13 (API 33)** ou superior.

## 📈 Destaques Técnicos para Review

1. **Separação de Modelos**: Note que existem modelos diferentes para API (`DTO`), Banco (`Entity`) e Domínio. Isso evita que mudanças na API quebrem o banco ou a UI.
2. **Resiliência de Rede**: O tratamento de erros em `util/NetworkError` mapeia exceções específicas de rede para estados de UI amigáveis, permitindo que o usuário entenda se o problema é o Wi-Fi ou o Servidor.
3. **UX Adaptativa**: O layout de detalhes do time (`DriversScreen`) foi otimizado para não "quebrar" em modo horizontal, usando técnicas de scroll unificado.
