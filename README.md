# Scanora

![Android](https://img.shields.io/badge/platform-Android-2E7D8C)
![Kotlin](https://img.shields.io/badge/Kotlin-2.3.10-23414B)
![Compose](https://img.shields.io/badge/Jetpack%20Compose-2026.03.00-DD8A2E)
![Version](https://img.shields.io/badge/version-0.1.4-133942)
[![Android CI](https://github.com/Soturine/scanora/actions/workflows/android-ci.yml/badge.svg)](https://github.com/Soturine/scanora/actions/workflows/android-ci.yml)
[![Deploy Pages](https://github.com/Soturine/scanora/actions/workflows/pages.yml/badge.svg)](https://github.com/Soturine/scanora/actions/workflows/pages.yml)

Scanora é um app Android de escaneamento de documentos com foco em processamento local, OCR no dispositivo e um fluxo enxuto de captura, revisão e exportação.

Repositório: https://github.com/Soturine/scanora  
Releases: https://github.com/Soturine/scanora/releases  
Site: https://soturine.github.io/scanora/

## O que o app já faz

- captura manual com CameraX e fluxo guiado com ML Kit Document Scanner;
- ajuste de corte, correção de perspectiva e filtros para documentos;
- OCR local com cópia rápida do texto;
- exportação em PDF, JPG e PNG com compartilhamento nativo;
- histórico local com título, tags, favoritos e busca;
- tema claro/escuro e arquitetura multimódulo pronta para evolução.

## Proposta de valor

Scanora foi pensado para quem quer transformar páginas, contratos e recibos em arquivos legíveis sem depender de upload obrigatório. A proposta é sair da câmera até o arquivo final com o mínimo de atrito e com privacidade por padrão.

## Capturas

Capturas reais em breve.  
O projeto já está validado com build Android, lint e testes unitários no CI.

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

`0.1.4` estabiliza o fluxo manual com crop mais confiável, filtros mais claros e uma base menos propensa a travamentos. Ainda faltam capturas reais, o fluxo rápido com o scanner do Google e mais polimento de OCR para uma release 1.0.

## Contribuir

Consulte [CONTRIBUTING.md](CONTRIBUTING.md), [CODE_OF_CONDUCT.md](CODE_OF_CONDUCT.md), [SECURITY.md](SECURITY.md) e [ROADMAP.md](ROADMAP.md).
