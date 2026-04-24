# Escopo do Scanora

## Fonte principal desta rodada

Este arquivo e a referencia de produto para a versao `v0.2.3`.

A direcao da rodada e transformar o pos-scan em uma experiencia clara, util, limpa e rapida. O scanner rapido do Google/ML Kit Document Scanner continua sendo o caminho principal de entrada, o modo manual continua como fallback editavel, e a evolucao desta versao se concentra no que acontece depois do scan: revisao, OCR, exportacao e pos-exportacao.

## Estrategia de produto

O Scanora nao deve competir com o scanner do Google no que ele ja faz melhor. O valor principal do app fica na experiencia ao redor do scan:

- entrada rapida pela Home;
- lote local criado a partir do resultado do scanner;
- revisao clara das paginas;
- ajuste manual quando necessario;
- OCR legivel, estruturado e copiavel;
- exportacao local em PDF, JPG ou PNG com opcoes progressivas;
- pos-exportacao com nome, tipo, tamanho, local salvo, abrir e compartilhar;
- historico local simples, pesquisavel e privado.

## Verdades atuais

- O scanner do Google entrega crop e resultado final melhores na maioria dos cenarios reais.
- O auto crop manual/importado e util como ponto de partida, mas nao deve ser vendido como caminho principal.
- URIs temporarias de scanner/galeria precisam ser copiadas para armazenamento interno antes de entrar no lote.
- O fluxo manual precisa continuar funcional para capturas proprias, fotos dificeis, cadernos e correcoes finas.
- A Home deve apontar primeiro para `Escanear rapido` e so depois para `Ajuste manual`.
- A partir da `v0.2.3`, OCR, revisao e exportacao precisam parecer fluxo de produto, nao uma tela tecnica de MVP.

## Escopo obrigatorio da v0.2.3

### 1. OCR util e estruturado

- ML Kit Text Recognition continua sendo o motor.
- A UI deve usar blocos e linhas quando a engine retornar estrutura util.
- `Copiar tudo` deve ficar evidente e funcionar com poucos toques.
- Copia por bloco pode existir sem competir com a acao principal.
- Estados de carregamento, vazio e erro precisam ser curtos e legiveis.

### 2. Exportacao com disclosure progressivo

- A escolha principal vem primeiro: `PDF` ou `Imagem`.
- Qualidade aparece apenas para PDF.
- JPG/PNG aparecem apenas quando `Imagem` estiver selecionado.
- O CTA principal precisa deixar claro se vai gerar PDF ou salvar imagens.

### 3. Pos-exportacao compreensivel

- Depois de exportar, mostrar:
  - nome do arquivo;
  - tipo;
  - tamanho;
  - onde foi salvo;
  - botao de abrir;
  - botao de compartilhar.
- Feedback de sucesso e erro deve ser curto, sem linguagem tecnica desnecessaria.

### 4. Revisao menos poluida

- Preview da pagina e exportacao devem dominar a hierarquia.
- Nome e tags ficam em disclosure progressivo.
- Textos de apoio devem ser reduzidos.
- Acoes secundarias continuam disponiveis sem transformar a revisao em formulario.

## Entregue na v0.2.2

- Home reorganizada para concentrar o topo em `Escanear rapido`.
- Manual/importacao reposicionados em `Ajuste manual`.
- Scanner rapido segue com galeria habilitada via `GmsDocumentScannerOptions`.
- Resultado de scanner, galeria e CameraX passa a ser copiado para `filesDir/scan-sources` antes de criar o lote.
- Titulos de lote indicam a origem: `Scan rapido`, `Modo manual` ou `Importacao manual`.
- Tela manual deixou de oferecer um atalho concorrente para o scanner guiado.
- OCR ganhou `Copiar tudo` no rodape, feedback de copia e blocos separados de leitura.
- Exportacao ganhou feedback curto de sucesso ao concluir.

## Entregue na v0.2.3

- Contrato de OCR passou a devolver `OcrTextResult` com blocos e linhas, mantendo `ocrText` como texto completo salvo no Room.
- Tela de OCR passou a renderizar blocos/linhas, com `Copiar tudo` no rodape e `Copiar bloco` em cada bloco.
- Exportacao passou a escolher primeiro entre `PDF` e `Imagem`.
- Opcoes de qualidade aparecem apenas para PDF.
- Opcoes JPG/PNG aparecem apenas no fluxo de imagem.
- Pos-exportacao mostra nome, tipo, tamanho, local, caminho quando disponivel, abrir e compartilhar por arquivo.
- Revisao colocou `Exportar lote` como acao principal, reduziu textos e manteve nome/tags em detalhes do lote.

## Fora do escopo desta rodada

- Reescrever o pipeline local de crop.
- Adicionar OpenCV, backend, sync, analytics ou bibliotecas pesadas.
- Prometer OCR perfeito.
- Gerar pipeline de assinatura Play Store.
- Trocar a modularizacao atual.

## Roadmap imediato apos v0.2.3

- QA visual em dispositivo real.
- Capturas publicas oficiais.
- Testes instrumentados para scanner/revisao/OCR/exportacao.
- Limpeza futura de arquivos fonte orfaos quando scans forem removidos.
- Melhorias incrementais no crop manual para cenarios de caderno, espiral e fundo poluido.
- Ajustes finos de responsividade nas telas de OCR/exportacao em aparelhos menores.
