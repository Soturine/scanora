# Arquitetura

## Visão geral

O projeto foi dividido em módulos para manter o app simples no MVP, mas já com fronteiras claras entre domínio, dados e interface.

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
  Entrada do produto com CTA principal de scanner rápido, fallback manual e lista recente.
- `feature-camera`
  Captura manual com CameraX.
- `feature-editor`
  Ajuste de cantos, filtros, rotação e revisão do lote.
- `feature-export`
  Seleção de formato/qualidade, geração de arquivos e pós-exportação.
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

O app usa duas estratégias complementares:

- `ML Kit Document Scanner`
  Caminho principal de captura rápida quando disponível, com experiência guiada e menor atrito.
- Pipeline local em `DefaultDocumentProcessingRepository`
  Voltado para captura manual e importação da galeria, com:
  - estimativa heurística de quadrilátero por bordas, brilho e amostras laterais;
  - warp de perspectiva com `Matrix.setPolyToPoly`;
  - normalização local de iluminação;
  - filtros recalibrados para documento, cinza, colorido e recibo;
  - prévia em duas etapas com cache;
  - saída dedicada para OCR e limpeza de borda preta.

## OCR

O OCR local continua em ML Kit Text Recognition, mas a imagem enviada para reconhecimento não depende mais apenas do filtro salvo da página. A `0.2.0` passa a preparar uma versão específica para leitura antes de chamar a engine.

## Exportação

PDF, JPG e PNG continuam sendo gerados localmente. Em Android 10+ a saída vai para `Downloads/Scanora`, enquanto versões anteriores usam o armazenamento do app. A camada de exportação devolve metadados para a UI mostrar nome, tipo, tamanho e local salvo.

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
  DI manual reduz atrito inicial e mantém o projeto legível para contribuição.
- Room + DataStore:
  cobertura suficiente para offline-first sem backend.
- Scanner híbrido:
  o fluxo rápido cobre a maioria dos casos e o manual segue como fallback editável.
- Coordenadas normalizadas:
  simplificam preview, edição manual e reprocessamento em diferentes resoluções.
