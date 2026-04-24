# Roadmap — Scanora

Este roadmap prioriza estabilidade, usabilidade e qualidade real do fluxo de escaneamento antes de adicionar complexidade desnecessária.

## Estratégia

1. manter o scanner rápido do Google como caminho principal
2. preservar o fluxo manual como fallback editável e confiável
3. polir performance, OCR, exportação e material público

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

**Status:** concluída em 2026-04-23

**Objetivo:** polir qualidade perceptível do app, endurecer o caminho manual/importado e deixar OCR e exportação mais próximos de um scanner utilizável de verdade.

### Entregue

- heurística inicial de crop refeita para sair de um retângulo aberto demais e estimar um quadrilátero mais útil
- filtros recalibrados para documento real, com menos página estourada, menos cinza artificial e prévia mais fiel
- OCR passou a usar imagem preparada especificamente para leitura, em vez de depender só do filtro salvo na página
- revisão de lote simplificada com foco na página em destaque, menos peso visual e ações mais diretas
- exportação com pós-exporto mais claro: nome, tipo, tamanho, local salvo e ação para abrir o arquivo
- arquivos exportados agora vão para `Downloads/Scanora` em Android 10+
- cache leve em memória para previews e thumbnails, reduzindo custo visual em navegação
- Home reforça o scanner rápido como caminho recomendado, mantendo manual e galeria como fallback claro

### Resultado esperado desta release

- auto crop manual/importado fica mais útil no primeiro palpite
- filtros deixam de piorar documentos em vários cenários comuns
- OCR fica menos poluído e mais prático para copiar texto
- exportação deixa claro onde o arquivo foi salvo
- o app transmite mais confiança no fluxo completo, da revisão ao pós-export

### Observação desta rodada

- as capturas oficiais em aparelho real seguem como fechamento natural de QA visual, mas não foram forçadas nesta máquina depois de instabilidade do emulador

---

## v0.2.1 — Menos Ruído, Mais Confiança

**Status:** concluída em 2026-04-23

**Objetivo:** endurecer o fluxo manual/importado, deixar os filtros menos destrutivos e reduzir o peso visual das telas que ainda pareciam MVP configurável.

### Entregue

- auto crop manual/importado agora compara mais candidatos antes de escolher o quadrilátero inicial;
- heurísticas de borda ficaram mais centradas na área útil da página, com menos influência de ruído nas extremidades;
- editor ganhou botão de reajuste automático, overlay mais claro e handles mais confortáveis;
- filtros foram recalibrados de novo para preservar melhor páginas pautadas, manuscrito e contraste de estudo;
- revisão passou a esconder nome e tags até o momento em que o usuário realmente quiser editar;
- exportação ficou mais dependente da escolha principal, com menos opções simultâneas e pós-export mais direto;
- OCR foi reorganizado para leitura contínua e cópia mais rápida.

### Resultado esperado desta release

- o fluxo manual fica mais confiável como fallback real quando o scanner rápido não basta;
- filtros deixam de piorar documentos em mais casos práticos de galeria e caderno;
- revisão, OCR e exportação passam menos sensação de tela pesada e mais de ferramenta rápida;
- o app se aproxima mais de um scanner utilizável no dia a dia sem inflar a arquitetura.

---

## v0.2.2 — Scanner Rápido no Centro

**Status:** concluída em 2026-04-24

**Objetivo:** consolidar `Escanear rápido` como fluxo principal do Scanora, manter o manual como fallback editável e corrigir a dependência de URIs temporárias vindas do scanner, galeria ou CameraX.

### Entregue

- Home reorganizada com CTA principal único para `Escanear rápido`;
- galeria continua habilitada pelo fluxo do Google quando o dispositivo oferece suporte;
- manual/importação direta foram reposicionados em `Ajuste manual`;
- imagens de entrada agora são copiadas para `filesDir/scan-sources` antes de criar o lote no histórico local;
- scanner rápido cria lote com origem clara e abre direto na revisão;
- tela manual deixou de mostrar atalho concorrente para o scanner guiado;
- OCR ganhou botão forte `Copiar tudo`, feedback curto e blocos de leitura mais claros;
- exportação ganhou feedback curto de sucesso mantendo qualidade visível apenas para PDF;
- `escopo.md` da raiz passou a ser a referência principal de produto desta fase.

### Resultado esperado desta release

- o app deixa de parecer dividido entre dois fluxos principais;
- importações e scans recentes continuam úteis mesmo depois que URIs temporárias deixam de existir;
- revisão, OCR e exportação ficam mais coerentes com um fluxo rápido de scanner;
- o fallback manual segue disponível sem tomar o lugar do scanner do Google.

---

## Depois da v0.2.2

### Candidatos para uma fase futura

- QA visual final em aparelho e publicação das capturas oficiais
- baseline profiles, benchmarks e tuning fino de performance
- presets mais avançados por tipo de documento
- melhorias reais de PDF e compressão por lote
- criptografia local opcional para lotes sensíveis

### Itens que ficam para depois de estabilizar o básico

- OpenCV
- Koin
- PdfBox
- pipeline premium de filtros próprios
