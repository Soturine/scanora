# Scanora

![Android](https://img.shields.io/badge/platform-Android-2E7D8C)
![Kotlin](https://img.shields.io/badge/Kotlin-2.1.21-23414B)
![Compose](https://img.shields.io/badge/Jetpack%20Compose-2026.03.00-DD8A2E)
![Version](https://img.shields.io/badge/version-0.1.0-133942)
![Status](https://img.shields.io/badge/status-MVP%20ativo-2E7D8C)

Scanora é um app Android original de escaneamento de documentos com foco em privacidade, processamento local e uma experiência rápida de captura, revisão e exportação.

## Proposta de valor

- Escanear documentos, recibos e páginas de estudo sem depender de upload obrigatório.
- Corrigir perspectiva, aplicar filtros e organizar lotes localmente.
- Exportar PDF, JPG e PNG com fluxo pensado para MVP evolutivo.
- Executar OCR local para copiar texto reconhecido com poucos toques.

## Destaques do MVP

- Fluxo híbrido com `CameraX` para captura manual e `ML Kit Document Scanner` para scanner guiado.
- OCR com `ML Kit Text Recognition`.
- Organização local com `Room`, busca por título, favoritos e tags.
- Preferências locais com `DataStore`.
- Exportação local com `PdfDocument` e compartilhamento via `FileProvider`.
- Tema claro/escuro e onboarding curto em português do Brasil.

## Screenshots

Placeholders iniciais:

- ![Home](docs/assets/screenshots/home.svg)
- ![Captura](docs/assets/screenshots/camera.svg)
- ![Editor](docs/assets/screenshots/editor.svg)
- ![Histórico](docs/assets/screenshots/history.svg)

## Stack

- Kotlin
- Android Gradle Plugin 9.1.0
- Gradle 9.3.1 Wrapper
- Jetpack Compose + Material 3
- Navigation Compose
- ViewModel + Coroutines + Flow
- Room
- DataStore
- WorkManager
- CameraX
- ML Kit Document Scanner
- ML Kit Text Recognition

## Arquitetura resumida

Estrutura modular usada no repositório:

- `app`: entrypoint, navegação, onboarding, splash e composição dos módulos.
- `core-common`: modelos, contratos de repositório e use cases centrais.
- `core-data`: Room, DataStore, OCR, exportação e pipeline local de imagem.
- `core-ui`: tema e componentes reutilizáveis de UI.
- `feature-home`, `feature-camera`, `feature-editor`, `feature-export`, `feature-history`, `feature-settings`, `feature-ocr`: telas e ViewModels por contexto funcional.

Mais detalhes em [docs/architecture.md](docs/architecture.md).

## Como rodar

1. Abra o projeto no Android Studio mais recente com suporte a AGP 9.1.
2. Use JDK 17 no Gradle.
3. Instale Android SDK Platform 36 e Build Tools compatíveis.
4. Rode `./gradlew assembleDebug` ou use o botão Run do Android Studio.

Setup detalhado: [docs/setup.md](docs/setup.md)

## Privacidade

- Processamento local por padrão.
- OCR e filtros são executados no dispositivo sempre que possível.
- O scanner guiado do ML Kit depende de componentes do Google Play services no aparelho.
- O app não exige backend nem conta para funcionar no MVP.

Leia a política em [PRIVACY_POLICY.md](PRIVACY_POLICY.md).

## Limitações atuais

- O pipeline local de detecção de documento ainda é heurístico para importações/capturas manuais.
- O scanner guiado depende da disponibilidade do ML Kit no dispositivo.
- O ambiente desta entrega não possui Android SDK instalado, então a compilação final não pôde ser validada localmente aqui.
- Ainda não há sincronização em nuvem, criptografia em repouso ou edição colaborativa.

## Contribuição

Contribuições são bem-vindas. Consulte:

- [CONTRIBUTING.md](CONTRIBUTING.md)
- [CODE_OF_CONDUCT.md](CODE_OF_CONDUCT.md)
- [SECURITY.md](SECURITY.md)

## Roadmap

Resumo curto:

- Curto prazo: melhorar detecção de contornos, UX de crop e qualidade de exportação.
- Médio prazo: lote multi-importação mais robusto, favoritos avançados e mais testes.
- Longo prazo: preparação de release 1.0, hardening de storage e refinamento de OCR.

Versão completa em [ROADMAP.md](ROADMAP.md).

## Status do projeto

`0.1.0` representa um MVP funcional e escalável, pronto para refinamento técnico e polimento de produção.

