# Changelog

Este projeto segue Semantic Versioning e recomenda Conventional Commits no fluxo de contribuição.

## [Unreleased]

## [0.1.5] - 2026-04-22

### Added

- Scanner rápido com `ML Kit Document Scanner` promovido a CTA principal na Home.
- Pipeline de prévia em duas etapas no editor, com render inicial rápido e refinamento assíncrono em background.

### Changed

- Sugestão inicial de quadrilátero do fluxo manual foi fortalecida para imagens importadas e capturas mais tortas.
- Filtros locais foram recalibrados para reduzir página lavada, texto apagado e diferenças pouco úteis entre presets.
- Tela de revisão foi reorganizada para dar mais foco em lote, páginas e ações principais.
- Tela de exportação passou a usar hierarquia mais direta, com navegação clara, CTA fixa e compartilhamento melhor integrado.
- Tela de OCR ficou mais legível e o processamento local passou a usar uma imagem melhor preparada para reconhecimento.

### Fixed

- Risco de recalcular a mesma prévia de filtro em recomposições sucessivas ou trocas rápidas de opção.
- Sensação de fluxo quebrado entre scanner guiado, revisão e histórico local.
- Estado antigo de arquivos exportados permanecendo visível depois de trocar formato ou qualidade.

## [0.1.4] - 2026-04-22

### Added

- Prévia leve e assíncrona para filtros, usando bitmap reduzido antes da aplicação final.
- Estado dedicado de prévia no editor para separar seleção visual de processamento definitivo.

### Changed

- `CropScreen` agora mostra a imagem original inteira com `ContentScale.Fit` e overlay alinhado à área real do preview.
- `FilterScreen` foi reorganizada com `Scaffold`, `TopAppBar` com voltar, preview central mais limpo e CTA fixa no rodapé.
- Pipeline local de detecção inicial de cantos foi reforçado com suavização de luma, projeção de bordas e limites mais estáveis.
- Carregamento de imagem assíncrono passou a usar decode em background e amostragem menor para reduzir custo visual.

### Fixed

- Ajuste manual dos quatro pontos deixou de usar coordenadas desalinhadas com o preview e ficou mais responsivo ao toque.
- Handles do crop ganharam área de toque maior e perderam a sensação de arrasto preso durante o gesto.
- Risco de ANR na tela de filtros foi reduzido ao evitar decode pesado e processamento síncrono na main thread.
- Fluxo do editor ficou mais direto, com menos texto e com ações principais mais claras.

## [0.1.3] - 2026-04-22

### Added

- Novo conjunto de componentes visuais compartilhados em `core-ui` para seções, opções e hierarquia de conteúdo.
- Hero mais forte na Home, novas superfícies de detalhe e OCR mais legível com prévia da página.

### Changed

- Tema visual refinado com paleta, formas e tipografia mais coerentes com a proposta de scanner de documentos.
- Site público em `site/` redesenhado para ficar mais leve, mais escaneável e mais próximo de uma página de produto real.
- README reorganizado com topo mais enxuto, seção objetiva do que o app já faz e melhor apresentação do CI.

### Fixed

- Microcopy PT-BR revisada nas telas principais para reduzir tom genérico e melhorar clareza das chamadas para ação.
- Estados vazios, cards de histórico, seleção de opções e revisão de lote com melhor ritmo visual e consistência.

## [0.1.2] - 2026-04-22

### Added

- Estrutura `planning/` para documentos de escopo e planejamento.
- Diretório `site/` dedicado ao GitHub Pages, separado da documentação técnica.

### Changed

- Rename completo de package, namespace e `applicationId` para `com.soturine.scanora`.
- README, site e workflows foram revisados para deixar a apresentação pública mais direta e consistente.
- `docs/` passou a conter apenas documentação técnica em Markdown.

### Fixed

- Links e templates que ainda apontavam para placeholders antigos ou para caminhos legados do site.
- Workflow do GitHub Pages agora publica `site/` em vez de `docs/`.
- Referências internas do projeto foram alinhadas ao package real sem quebrar imports.

### Planned

- Substituir mockups e placeholders por capturas reais do app em emulador ou dispositivo.
- Refinar o material de release e o checklist para publicação 1.0.

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

- Rodar revisão visual em emulador ou dispositivos reais para validar espaçamento, responsividade e estados extremos.
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
