# Arquitetura

## Visão geral

O projeto foi dividido em módulos para manter o MVP simples, mas já com fronteiras claras entre domínio, dados e interface.

## Módulos

- `app`
  Responsável por `Application`, container de dependências, splash, onboarding e `NavHost`.
- `core-common`
  Contém modelos de domínio, contratos de repositório, resultado/validação e use cases centrais.
- `core-data`
  Implementa Room, DataStore, OCR, exportação, workers e pipeline local de imagem.
- `core-ui`
  Define tema, paleta, tipografia e componentes reutilizáveis de Compose.
- `feature-home`
  Entrada do produto com CTA, escolha de modo, importação e lista recente.
- `feature-camera`
  Captura manual com CameraX e scanner guiado do ML Kit.
- `feature-editor`
  Ajuste de cantos, filtros, rotação e revisão do lote.
- `feature-export`
  Seleção de formato/qualidade e geração de arquivos.
- `feature-history`
  Histórico pesquisável e detalhe do scan salvo.
- `feature-settings`
  Preferências locais e tela sobre.
- `feature-ocr`
  Reconhecimento de texto e cópia rápida.

## Fluxo de dados

1. A UI dispara eventos para um `ViewModel`.
2. O `ViewModel` conversa com contratos definidos em `core-common`.
3. As implementações concretas em `core-data` acessam Room, DataStore, OCR, exportação ou processamento de imagem.
4. O resultado volta como `Flow`/estado para a UI.

## Persistência

- `Room`
  Guarda scans, páginas, tags serializadas, favoritos, timestamps e estado de rascunho.
- `DataStore`
  Guarda onboarding, tema, modo padrão e qualidade padrão do PDF.

## Pipeline de imagem

O MVP usa duas estratégias:

- `ML Kit Document Scanner`
  Fluxo guiado com detecção automática, crop e edição integrada quando disponível.
- Pipeline local em `DefaultDocumentProcessingRepository`
  Voltado para captura manual/importação, com:
  - estimação heurística do documento;
  - warp de perspectiva com `Matrix.setPolyToPoly`;
  - normalização de iluminação;
  - filtros para documento, cinza, colorido e recibo;
  - limpeza de borda preta e sharpen leve.

## Dependências principais

- Compose + Material 3
- Navigation Compose
- Lifecycle ViewModel / Runtime Compose
- Room
- DataStore
- WorkManager
- CameraX
- ML Kit Document Scanner
- ML Kit Text Recognition

## Rationale técnico

- Sem Hilt no MVP:
  manual DI reduz atrito inicial e mantém o projeto legível para contribuição.
- Room + DataStore:
  cobertura suficiente para offline-first sem backend.
- CameraX + ML Kit:
  atende o requisito de câmera própria e ainda oferece um caminho guiado de melhor UX.
- Módulos por feature:
  mantêm o app preparado para crescer sem virar monólito de telas.

