# Scanora

![Android](https://img.shields.io/badge/platform-Android-2E7D8C)
![Kotlin](https://img.shields.io/badge/Kotlin-2.3.10-23414B)
![Compose](https://img.shields.io/badge/Jetpack%20Compose-2026.03.00-DD8A2E)
![Version](https://img.shields.io/badge/version-0.2.3-133942)
[![Android CI](https://github.com/Soturine/scanora/actions/workflows/android-ci.yml/badge.svg)](https://github.com/Soturine/scanora/actions/workflows/android-ci.yml)
[![Deploy Pages](https://github.com/Soturine/scanora/actions/workflows/pages.yml/badge.svg)](https://github.com/Soturine/scanora/actions/workflows/pages.yml)

Scanora é um app Android de digitalização de documentos com foco em processamento local, OCR no dispositivo e um fluxo direto entre captura, revisão e exportação.

Repositório: https://github.com/Soturine/scanora  
Releases: https://github.com/Soturine/scanora/releases  
Site: https://soturine.github.io/scanora/

## O que o app já faz

- scanner rápido com `ML Kit Document Scanner` como fluxo principal direto na Home;
- importação de galeria pelo fluxo do Google quando suportada;
- captura manual com `CameraX` e importação direta como fallback editável;
- cópia das imagens de entrada para armazenamento interno antes de criar o lote local;
- sugestão inicial de crop mais robusta para fotos inclinadas, galeria e fundos poluídos;
- reajuste automático do crop e editor manual mais confortável para acertos finos;
- filtros locais recalibrados para documento, cinza, cor e recibo com menos risco de estourar a página;
- OCR local com imagem preparada, leitura por blocos e `Copiar tudo` em destaque;
- exportação em PDF, JPG e PNG com escolha progressiva entre PDF e Imagem;
- pós-exportação com nome, tipo, tamanho, local salvo, abrir e compartilhar;
- histórico local com título, tags, favoritos e busca.

## Proposta de valor

Scanora foi pensado para transformar páginas, contratos, cadernos e recibos em arquivos legíveis sem depender de upload obrigatório. O scanner rápido virou o caminho principal, mas o fluxo manual continua disponível quando o documento precisa de ajuste fino.

## Capturas

Capturas oficiais do app em aparelho real seguem em validação final.  
Nesta rodada, o material público foi alinhado ao fluxo real do produto sem substituir essa etapa por mockups artificiais.

## Stack

- Kotlin
- Android Gradle Plugin 9.1.0
- Jetpack Compose + Material 3
- Navigation Compose
- ViewModel + Coroutines + Flow
- Room
- DataStore
- WorkManager
- CameraX
- ML Kit Document Scanner
- ML Kit Text Recognition

## Arquitetura

- `app`: bootstrap, navegação, onboarding e integração dos módulos
- `core-common`: modelos, contratos e use cases
- `core-data`: Room, DataStore, OCR, exportação e processamento de imagem
- `core-ui`: tema e componentes reutilizáveis
- `feature-*`: telas e ViewModels por contexto funcional

Referências técnicas:

- [docs/architecture.md](docs/architecture.md)
- [docs/decisions.md](docs/decisions.md)
- [docs/setup.md](docs/setup.md)
- [docs/testing.md](docs/testing.md)
- [docs/publishing.md](docs/publishing.md)

## Como rodar

1. Abra o projeto no Android Studio com suporte a AGP 9.1.
2. Use JDK 17 ou superior compatível com AGP 9.
3. Instale Android SDK Platform 36 e Build Tools 36.0.0.
4. Rode `./gradlew assembleDebug` ou execute o módulo `app`.

Identidade do app:

- `applicationId`: `com.soturine.scanora`
- namespace base: `com.soturine.scanora`

## CI e Pages

- O workflow [Android CI](https://github.com/Soturine/scanora/actions/workflows/android-ci.yml) builda o projeto, roda lint e testes unitários.
- O site público é publicado a partir de `site/`.
- Para o GitHub Pages funcionar no repositório publicado, ative em `Settings > Pages > Source: GitHub Actions`.

## Privacidade

- processamento local por padrão;
- OCR e filtros executados no dispositivo sempre que possível;
- sem backend obrigatório, login ou sincronização no MVP.

Política completa em [PRIVACY_POLICY.md](PRIVACY_POLICY.md).

## Status

`0.2.3` mantém o scanner rápido como fluxo principal e melhora o pós-scan: OCR por blocos, revisão menos poluída, exportação progressiva e estado final com arquivo, tipo, tamanho, local salvo, abrir e compartilhar. O próximo passo natural continua sendo fechar QA visual em dispositivo e publicar capturas oficiais.

## Contribuir

Consulte [CONTRIBUTING.md](CONTRIBUTING.md), [CODE_OF_CONDUCT.md](CODE_OF_CONDUCT.md), [SECURITY.md](SECURITY.md) e [ROADMAP.md](ROADMAP.md).
