# Escopo do Scanora

## Fonte principal desta fase

Este arquivo é a referência de produto para a evolução do Scanora após a `v0.2.5`.

A `v0.2.5` consolidou o OCR manual/importado com pós-processamento, texto organizado por trechos e cópia consolidada. A partir daqui, o foco segue em confiabilidade visual, coerência do pipeline e qualidade percebida sem adicionar complexidade antes da hora.

O scanner rápido do Google/ML Kit Document Scanner continua sendo o caminho principal de entrada. O modo manual continua existindo como fallback editável para casos em que o usuário precisa controlar corte, rotação, filtro e OCR página por página.

---

## Norte do produto

O Scanora deve ser um scanner Android simples, rápido e confiável para:

- escanear ou importar documentos;
- revisar páginas com clareza;
- ajustar corte e visual quando necessário;
- copiar texto por OCR de forma útil;
- exportar PDF, JPG ou PNG sem atrito;
- manter histórico local e privado.

O objetivo não é competir com o scanner do Google no que ele já faz melhor. O valor do Scanora está na experiência ao redor do scan:

- entrada rápida;
- fallback manual;
- revisão limpa;
- OCR copiável;
- exportação clara;
- histórico local;
- privacidade;
- identidade visual própria.

---

## Estratégia de produto

### Caminho principal

O caminho principal deve ser:

Home -> Escanear rápido -> Scanner do Google -> Revisão -> OCR ou Exportação

### Caminho secundário

O caminho secundário deve ser:

Home -> Ajuste manual / Importar -> Crop manual -> Filtros -> Revisão -> OCR ou Exportação

### Decisão de produto

O modo manual é importante, mas não deve parecer o caminho principal. Ele deve ser apresentado como ferramenta de ajuste fino, não como obrigação para o usuário comum.

---

## Verdades atuais

- O scanner do Google entrega crop, alinhamento e filtros melhores na maioria dos cenários reais.
- O auto crop manual/importado ainda é útil como ponto de partida, mas continua mais fraco que o scanner do Google.
- Fotos de caderno, espiral, fundo poluído, manuscrito e mesa com objetos ainda quebram heurísticas com facilidade.
- O app ainda pode parecer visualmente poluído em algumas telas.
- A Home ainda pode ser mais direta.
- A seleção antecipada entre tipos como documento, recibo e caderno aumenta atrito.
- O OCR por blocos melhorou, mas o fluxo manual ainda pode gerar fragmentos pequenos demais.
- Preview, filtro, OCR e exportação precisam usar uma base de imagem coerente para evitar zoom, distorção ou diferença entre o que o usuário vê e o que sai no arquivo.
- A identidade visual ainda pode ficar mais forte com uma mascote/raposa e ícone próprio.

---

## Princípios obrigatórios

- Menos configuração, mais resultado.
- Menos texto, mais ação.
- Menos opções visíveis ao mesmo tempo.
- Scanner rápido como padrão.
- Manual como fallback editável.
- OCR útil, não apenas tecnicamente funcional.
- Exportação compreensível para usuário comum.
- Preview leve, resultado final em alta qualidade.
- Processamento pesado fora da main thread.
- Documentação sempre alinhada com a versão real.

---

## Regras técnicas

- Não reescrever o app do zero.
- Não quebrar a modularização atual.
- Não adicionar backend.
- Não adicionar OpenCV, Koin, PdfBox ou bibliotecas pesadas sem necessidade real.
- Não aplicar bitmap full-res em tempo real na UI.
- Preview pode usar resolução intermediária.
- Aplicação final, OCR final e exportação devem usar imagem correta e processada fora da UI.
- Não remover o fluxo manual.
- Não remover o scanner rápido.
- Não prometer OCR perfeito.
- Sempre validar build, testes e lint antes de concluir.

---

# Entregue até v0.2.5

## v0.2.2 — Scanner rápido no centro

- Home reorganizada para concentrar o topo em `Escanear rápido`.
- Manual/importação reposicionados em `Ajuste manual`.
- Scanner rápido segue com galeria habilitada via `GmsDocumentScannerOptions`.
- Resultado de scanner, galeria e CameraX passa a ser copiado para `filesDir/scan-sources` antes de criar o lote.
- Títulos de lote indicam a origem: `Scan rápido`, `Modo manual` ou `Importação manual`.
- Tela manual deixou de oferecer atalho concorrente para o scanner guiado.
- OCR ganhou `Copiar tudo` no rodapé, feedback de cópia e blocos separados de leitura.
- Exportação ganhou feedback curto de sucesso ao concluir.

## v0.2.3 — Pós-scan que parece produto

- Contrato de OCR passou a devolver resultado estruturado com blocos e linhas.
- Tela de OCR passou a renderizar blocos/linhas, com `Copiar tudo` no rodapé e `Copiar bloco` em cada bloco.
- Exportação passou a escolher primeiro entre `PDF` e `Imagem`.
- Opções de qualidade aparecem apenas para PDF.
- Opções JPG/PNG aparecem apenas no fluxo de imagem.
- Pós-exportação mostra nome, tipo, tamanho, local, caminho quando disponível, abrir e compartilhar por arquivo.
- Revisão colocou `Exportar lote` como ação principal, reduziu textos e manteve nome/tags em detalhes do lote.

---

# v0.2.4 — Minimalismo + identidade visual

**Status:** entregue em 2026-04-25.

## Objetivo

Deixar o Scanora mais limpo, direto e com identidade própria, reduzindo a sensação de MVP cheio de cards e opções.

Esta versão deve fazer o usuário entender rapidamente:

- onde tocar para escanear;
- quando usar o modo manual;
- onde acessar histórico;
- qual é a personalidade visual do app.

## Implementado na v0.2.4

- Home simplificada com `Escanear` como CTA principal absoluto.
- Escolha antecipada entre documento, recibo e caderno removida da Home.
- Scanner rápido usa modo universal no fluxo principal e continua abrindo revisão.
- Ajuste manual e importação direta ficam como fallback secundário.
- Onboarding refeita em 3 telas com as ilustrações `onboarding_scanora_scan`, `onboarding_scanora_adjust` e `onboarding_scanora_privacy`.
- Ícone do launcher atualizado com `scanora_icon_source`.
- Splash usa a nova identidade de forma discreta.
- `README.md`, `ROADMAP.md`, `CHANGELOG.md`, `docs/` e `site/` foram alinhados à versão `0.2.4`.

---

## Escopo obrigatório da v0.2.4

### 1. Home mais minimalista

- Simplificar a Home.
- Transformar `Escanear rápido` ou `Escanear` no CTA principal absoluto.
- Reduzir textos longos.
- Reduzir quantidade e peso visual dos cards.
- Deixar o ajuste manual como opção secundária.
- Deixar histórico visível, mas sem competir com o CTA principal.
- Evitar sensação de formulário ou painel técnico.

### 2. Remover escolha antecipada de tipo

- Remover da Home a escolha obrigatória entre `Documento`, `Recibo`, `Caderno` ou equivalentes.
- O usuário não deve precisar decidir o tipo antes de escanear.
- Usar modo padrão universal no fluxo principal.
- Se existir classificação por tipo, ela deve ser automática, discreta e posterior ao scan.
- Se houver opção para alterar tipo, ela deve ficar em área avançada ou dentro da revisão/filtros, não no primeiro contato.

### 3. Identidade visual com raposa

- Criar proposta de ícone com uma raposa escaneando ou interagindo com documento.
- O ícone deve ser legível em tamanho pequeno.
- O visual deve ser profissional, não infantil demais.
- Usar a raposa como mascote leve do Scanora.
- Reforçar a identidade visual sem poluir as telas.

### 4. Onboarding com mascote

- Refazer o onboarding em 3 telas simples.
- Usar a raposa/mascote nas telas.
- Cada tela deve comunicar apenas uma ideia:
  1. escanear rapidamente;
  2. revisar e ajustar;
  3. privacidade por padrão.
- Reduzir texto.
- Usar os PNGs fornecidos da raposa, preservando transparência e sem adicionar Lottie.
- Não adicionar Lottie ou bibliotecas extras sem necessidade.

### 5. Polimento visual geral

- Revisar espaçamentos, paddings, hierarquia de títulos e CTAs.
- Reduzir cards muito grandes quando não forem necessários.
- Evitar excesso de labels explicativas.
- Manter Material 3 + Compose.
- Garantir boa leitura em telas menores.
- Manter tema claro/escuro funcional.

### 6. Documentação

Atualizar quando aplicável:

- `README.md`
- `ROADMAP.md`
- `CHANGELOG.md`
- `site/`
- `AGENTS.md`, se as regras de identidade visual precisarem ser registradas.

---

## Critérios de pronto da v0.2.4

- A Home parece mais limpa e direta.
- Não existe escolha obrigatória entre documento/recibo/caderno antes de escanear.
- O CTA principal de scan é óbvio.
- O modo manual continua acessível, mas secundário.
- O onboarding usa a raposa/mascote e comunica melhor o produto.
- O ícone do app tem identidade própria.
- O app parece menos MVP e mais produto.
- Build, testes e lint passam.

---

# v0.2.5 — OCR manual confiável

**Status:** entregue em 2026-04-25.

## Objetivo

Tornar o OCR do fluxo manual/importado realmente útil, evitando texto quebrado em blocos minúsculos e melhorando a leitura copiável.

A `v0.2.3` estruturou OCR por blocos. A `v0.2.5` foi além: consolidou texto, agrupou linhas e reduziu ruído.

---

## Implementado na v0.2.5

- Resultado de OCR passou a preservar bounding boxes de blocos e linhas do ML Kit.
- Pós-processamento puro em `core-common` ordena linhas por posição visual, agrupa parágrafos e reduz ruídos pequenos.
- O modelo de OCR expõe texto consolidado, trechos/parágrafos, qualidade simples e contagem de ruído descartado.
- `Copiar tudo` usa o texto consolidado pós-processado.
- Tela de OCR ganhou visualização por `Trechos` e `Texto contínuo`, com microcopy mais curta.
- Cópia por trecho substitui a lógica visual de copiar blocos crus.
- Revisão mostra `OCR pronto` de forma discreta quando a página já tem texto salvo.
- Testes unitários cobrem ordenação, agrupamento, remoção de ruído e consolidação.

---

## Escopo obrigatório da v0.2.5

### 1. Pós-processamento de OCR

- Não exibir blocos crus como experiência principal quando eles estiverem fragmentados.
- Ordenar blocos e linhas por posição visual.
- Agrupar linhas próximas em parágrafos.
- Descartar ruídos muito pequenos quando forem claramente inúteis.
- Preservar texto completo em uma saída consolidada.
- Manter acesso ao texto completo salvo.

### 2. Modos de visualização

Criar ou ajustar a tela de OCR para permitir:

- `Texto contínuo`;
- `Parágrafos`;
- opcionalmente `Blocos originais` apenas como modo secundário/debug visual.

### 3. Cópia mais útil

- `Copiar tudo` deve usar o texto consolidado.
- `Copiar parágrafo` pode substituir ou complementar `Copiar bloco`.
- O usuário deve conseguir copiar o texto útil com poucos toques.
- Feedback de cópia deve ser curto.

### 4. Redução de ruído visual

- A tela de OCR deve ter menos cabeçalhos repetidos.
- A prévia da imagem não deve ocupar espaço excessivo quando o foco for texto.
- Evitar cards demais.
- Priorizar legibilidade.

### 5. OCR manual/importado

- Verificar se o OCR do modo manual usa a imagem corrigida apropriada.
- Evitar OCR em preview reduzida quando isso prejudicar resultado.
- Evitar OCR em imagem com filtro destrutivo quando houver base melhor disponível.
- Tratar casos de:
  - caderno manuscrito;
  - tela fotografada;
  - folha impressa;
  - recibo;
  - documento inclinado.

---

## Critérios de pronto da v0.2.5

- OCR manual não explode em dezenas de blocos inúteis.
- Texto contínuo fica legível.
- Parágrafos ficam mais naturais.
- `Copiar tudo` copia uma saída útil.
- A UI de OCR parece ferramenta prática, não despejo técnico.
- Build, testes e lint passam.

---

# v0.2.6 — Fidelidade da imagem e pipeline único

## Objetivo

Garantir que preview, corte, filtro, OCR, revisão e exportação trabalhem sobre a mesma definição lógica de página, evitando zoom, distorção, rotação errada ou diferença entre o que o usuário vê e o arquivo final.

---

## Escopo obrigatório da v0.2.6

### 1. Fonte única da verdade da página

Cada página deve ter um modelo claro com informações suficientes para reconstruir o resultado:

- `sourceUri`;
- tamanho original da imagem;
- pontos de crop normalizados;
- rotação;
- filtro selecionado;
- uri/cache do resultado processado quando existir;
- origem da página, quando útil.

### 2. Separar preview de resultado final

- Preview é apenas visual.
- Preview não deve virar base final de OCR/exportação.
- Preview pode ser reduzida/intermediária.
- Resultado final deve ser gerado a partir da fonte correta, fora da UI.
- OCR deve usar imagem adequada ao reconhecimento, sem depender de thumbnail visual.

### 3. Coerência entre telas

Garantir que o enquadramento seja coerente entre:

- tela de revisão;
- tela de filtros;
- tela de OCR;
- exportação final;
- arquivo salvo;
- compartilhamento.

### 4. Corrigir distorções

Investigar e corrigir:

- zoom inesperado;
- imagem torta depois do filtro;
- escala diferente entre revisão e filtro;
- crop reaplicado duas vezes;
- rotação aplicada em ordem errada;
- `ContentScale` inadequado;
- uso de bitmap intermediário como se fosse original.

### 5. Testes práticos

Validar pelo menos estes cenários:

- scan rápido de folha A4;
- importação de galeria;
- caderno com espiral;
- manuscrito;
- tela de notebook fotografada;
- recibo;
- imagem girada;
- fundo poluído.

---

## Critérios de pronto da v0.2.6

- A página mostrada em revisão bate com filtro, OCR e exportação.
- Não há zoom inesperado.
- Não há distorção visual evidente.
- OCR usa base correta.
- Exportação final corresponde ao que o usuário aprovou.
- O pipeline fica mais previsível para evoluções futuras.
- Build, testes e lint passam.

---

# Fora do escopo imediato

Até estabilizar `v0.2.6`, evitar:

- OpenCV;
- backend;
- sincronização em nuvem;
- login;
- assinatura Play Store;
- monetização;
- criptografia local avançada;
- editor avançado de PDF;
- assinatura em PDF;
- compressão premium;
- refactor completo da arquitetura.

---

# Roadmap após v0.2.6

## v0.3.0 — QA visual e material público

- QA visual em aparelho real.
- Capturas oficiais para README e site.
- Ajustes finais de responsividade.
- Revisão de microcopy.
- Preparação de release pública mais apresentável.

## v0.3.1 — Performance percebida

- Avaliar Baseline Profiles.
- Melhorar abertura fria.
- Reduzir jank em navegação.
- Melhorar cache de thumbnails.
- Medir gargalos reais em aparelho mediano.

## v0.4.0 — Preparação Play Store

- Checklist de privacidade.
- Política atualizada.
- Ícones finais.
- Screenshots finais.
- Descrição curta e longa.
- Teste fechado.
- Geração de bundle/release assinado.
