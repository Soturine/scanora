# Roadmap — Scanora

Este roadmap prioriza estabilidade, usabilidade e qualidade real do fluxo de escaneamento antes de adicionar complexidade desnecessária.

## Estratégia

1. primeiro corrigir UX e estabilidade do fluxo manual
2. depois integrar um fluxo rápido confiável com o motor do Google
3. depois polir OCR, performance e material público

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
- tela de filtros reorganizada com `Scaffold`, `TopAppBar` com voltar e CTA fixo
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

**Objetivo:** integrar o `ML Kit Document Scanner` como caminho rápido e confiável, sem remover o fluxo manual.

### Foco

- integrar `GmsDocumentScanner`
- criar um modo rápido para captura guiada
- encaixar o resultado no histórico local já existente
- manter o fluxo manual como fallback editável

### Critérios de pronto

- fluxo rápido confiável em aparelho real compatível
- captura → revisão → salvar com menos atrito
- histórico continua consistente entre scanner rápido e fluxo manual

---

## v0.2.0 — A Experiência Premium

**Objetivo:** polir OCR, performance percebida e material público do projeto.

### Foco

- OCR mais sólido no dispositivo
- melhorias de fluidez na abertura e navegação
- acabamento visual final nas telas principais
- capturas reais no README e no site

### Critérios de pronto

- OCR local estável para páginas tratadas
- app abre e navega com mais fluidez
- material público substitui placeholders por capturas reais

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
