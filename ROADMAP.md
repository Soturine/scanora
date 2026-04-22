# Roadmap — Scanora

Este roadmap prioriza estabilidade, usabilidade e qualidade real do fluxo de escaneamento antes de adicionar complexidade desnecessária.

## Estratégia

1. primeiro corrigir UX e estabilidade do fluxo manual
2. depois integrar um fluxo rápido confiável com o motor do Google
3. depois polir performance, OCR e material público

## Regras de evolução

- manter o package `com.soturine.scanora`
- manter a modularização atual
- evitar bibliotecas pesadas antes de estabilizar a base
- preservar processamento local sempre que possível
- não introduzir OpenCV, Koin ou PdfBox antes de existir necessidade real

---

## v0.1.4 — A Cura da Interface

**Status:** concluída em 2026-04-22

**Objetivo:** estabilizar o fluxo manual atual e corrigir os problemas mais visíveis de preview, crop, filtros e responsividade.

### Entregue

- preview do crop corrigido para mostrar a página inteira
- overlay do quadrilátero mapeado sobre a área real da imagem
- handles maiores e mais responsivos para ajuste manual
- sugestão inicial de cantos acionada automaticamente no fluxo manual
- heurística local de detecção de cantos fortalecida sem depender de OpenCV
- tela de filtros reorganizada com `Scaffold`, `TopAppBar` com voltar e CTA fixa
- prévia de filtros em bitmap reduzido e processamento fora da main thread
- redução do risco de ANR ao mover decode e pré-processamento para background
- microcopy mais curta e fluxo visual mais direto

### Resultado esperado desta release

- o usuário consegue ver a folha inteira antes de salvar o corte
- os quatro pontos respondem melhor ao toque e deixam de parecer presos
- a tela de filtros ficou mais clara para aplicar, girar e seguir
- o editor trabalha com prévias leves sem bloquear a interface

---

## v0.1.5 — O Salto de Qualidade

**Status:** concluída em 2026-04-22

**Objetivo:** consolidar o `ML Kit Document Scanner` como caminho rápido e confiável, sem remover o fluxo manual.

### Entregue

- `GmsDocumentScanner` integrado como CTA principal da Home
- scanner rápido agora cria lote local e abre direto em revisão
- captura manual preservada como fallback editável
- sugestão inicial de crop fortalecida para imagens importadas e fluxo manual
- prévia de filtros em duas etapas com cache, cancelamento de job e refinamento assíncrono
- filtros revisados para reduzir página lavada, texto apagado e presets pouco úteis
- revisão de lote simplificada com foco em páginas e ações principais
- exportação reorganizada com scroll confiável, CTA fixa e compartilhamento mais claro
- OCR local refinado com entrada melhor tratada e saída mais legível

### Resultado esperado desta release

- o fluxo rápido passou a ser o caminho mais direto na Home
- a revisão do lote ficou menos poluída e mais previsível
- a exportação sempre oferece um caminho claro até gerar e compartilhar arquivos
- a prévia dos filtros ficou mais representativa sem bloquear a interface
- OCR e histórico continuam funcionando no mesmo modelo local do MVP

---

## v0.2.0 — A Experiência Premium

**Objetivo:** polir performance percebida, QA visual real e acabamento público do projeto.

### Foco

- QA visual em aparelho real e capturas oficiais
- melhorias de fluidez na abertura, navegação e exportação
- acabamento visual final nas telas principais
- capturas reais no README e no site

### Critérios de pronto

- app abre e navega com mais fluidez
- material público substitui placeholders por capturas reais
- ajustes finais de OCR, exportação e compartilhamento em dispositivos reais

---

## Depois da v0.2.0

### Candidatos para uma fase futura

- presets mais avançados por tipo de documento
- melhorias reais de PDF e compressão por lote
- baseline profiles, benchmarks e tuning fino
- criptografia local opcional para lotes sensíveis

### Itens que ficam para depois de estabilizar o básico

- OpenCV
- Koin
- PdfBox
- pipeline premium de filtros próprios
