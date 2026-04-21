# Changelog

Este projeto segue Semantic Versioning e recomenda Conventional Commits no fluxo de contribuição.

## [0.1.1] - 2026-04-21

### Added

- Validação local do ambiente com Android SDK Platform 36, Build Tools 36.0.0 e build de linha de comando.
- Feedback visual mais claro na exportação com resumo do lote e indicador de progresso local.

### Changed

- Tela de revisão agora salva nome e tags sob ação explícita, em vez de persistir a cada tecla.
- Botões de seleção e reordenação de páginas passaram a refletir melhor o estado atual do lote.
- Documentação de setup e testes foi atualizada com o fluxo realmente validado nesta máquina.

### Fixed

- Redução de escritas redundantes no Room ao renomear documentos e atualizar tags sem alterações reais.
- Exibição de mensagens de erro na revisão, cobrindo validação de nome e falhas de persistência.
- Ajustes de build para AGP 9.1, Kotlin 2.3.10 e dependências necessárias para compilar, lintar e testar localmente.

### Planned

- Rodar revisão visual em emulador/dispositivos reais para validar espaçamento, responsividade e estados extremos.
- Ampliar cobertura de testes instrumentados para fluxos de revisão, OCR e exportação.

## [0.1.0] - 2026-04-21

### Added

- Base Android multimódulo com `app`, `core-*` e `feature-*`.
- Splash, onboarding, home, captura, editor, exportação, histórico, detalhe, OCR, configurações e sobre.
- Captura manual com `CameraX`.
- Scanner guiado com `ML Kit Document Scanner`.
- OCR local com `ML Kit Text Recognition`.
- Persistência local com `Room`.
- Preferências locais com `DataStore`.
- Exportação de PDF, JPG e PNG com compartilhamento por `FileProvider`.
- Histórico local com busca por título, tags e favoritos.
- Tema claro/escuro e componentes reutilizáveis em Compose.
- Site estático inicial para GitHub Pages.
- CI inicial para build, lint e testes unitários.
- Documentação inicial completa para setup, arquitetura, publicação e testes.

### Changed

- Escolha de pipeline híbrido: captura manual própria e scanner guiado complementar para acelerar o MVP.
- Coordenadas de crop normalizadas para simplificar preview, edição manual e processamento.

### Fixed

- Sanitização básica de nomes de documentos.
- Padronização inicial de datas exibidas em pt-BR.
- Geração previsível de nomes de arquivo para exportação.

### Planned

- Melhorar detecção de quadriláteros em imagens importadas.
- Refinar crop manual com snapping e validação visual.
- Adicionar mais testes instrumentados e cobertura de repositórios.
- Evoluir tratamento de erros e acessibilidade para release pública.
